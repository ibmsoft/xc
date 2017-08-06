/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;

import com.cubedrive.base.utils.BaseFileUtil;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventVMLDrawing;
import com.cubedrive.sheet.utils.SheetFileUtil;
import com.microsoft.schemas.office.excel.CTClientData;
import com.microsoft.schemas.vml.CTShape;

public class ReadEventSheetPicture {
    
    private final List<Map<String,Object>> _pictureSettings = new ArrayList<>();
    
    private ReadEventSheetPicture(){
        
    }
    
    public List<Map<String,Object>> getPictureSettings(){
        return _pictureSettings;
    }
    
    private static Map<String,Object> handlePicture(XSSFPicture pic, SheetImportContext sheetImportContext) throws IOException{
        XSSFPictureData pictureData = pic.getPictureData();
        String url = null;
        if(pictureData == null){
            String linkId = pic.getCTPicture().getBlipFill().getBlip().getLink();
            if(linkId != null){
                PackageRelationship externalImageRelationship = pic.getDrawing().getPackagePart().getRelationship(linkId);
                if(externalImageRelationship != null){
                    url = externalImageRelationship.getTargetURI().toURL().toExternalForm();
                }
            }
        }else{
            String imageName = FilenameUtils.getName(pictureData.getPackagePart().getPartName().getName());
            File pictureDir = SheetFileUtil.getUserExcelPictureDirByDocument(
                    String.valueOf(sheetImportContext.sheetImportEnvironment().documentFile().getAuthor().getId()),
                    String.valueOf(sheetImportContext.sheetImportEnvironment().documentFile().getId()));
            File imageFile = new File(pictureDir,imageName);
            Files.copy(pictureData.getPackagePart().getInputStream(), imageFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            url = BaseFileUtil.getPathUnderUserFileServer(imageFile);
        }
        if(url != null){
            Map<String,Object> setting = new HashMap<>();
            setting.put("url",url);
            XSSFClientAnchor anchor = (XSSFClientAnchor)pic.getAnchor();
            setting.put("sr", anchor.getRow1());
            setting.put("sc", anchor.getCol1());
            setting.put("er", anchor.getRow2());
            setting.put("ec", anchor.getCol2());
            setting.put("sx", (int)(anchor.getDx1()/9525));
            setting.put("sy", (int)(anchor.getDy1()/9525));
            setting.put("ex", (int)(anchor.getDx2()/9525));
            setting.put("ey", (int)(anchor.getDy2()/9525));
            setting.put("floorType","picture");
            return setting;
        }
        return null;
        
    }
    
    private static Map<String,Object> handleVMLShape(VMLShape vmlShape, SheetImportContext sheetImportContext)throws IOException{
    	PackagePart imagePackagePart = vmlShape.getVMLImageData();
    	if(imagePackagePart != null){
    		String imageName = FilenameUtils.getName(imagePackagePart.getPartName().getName());
            File pictureDir = SheetFileUtil.getUserExcelPictureDirByDocument(
                    String.valueOf(sheetImportContext.sheetImportEnvironment().documentFile().getAuthor().getId()),
                    String.valueOf(sheetImportContext.sheetImportEnvironment().documentFile().getId()));
            File imageFile = new File(pictureDir,imageName);
            Files.copy(imagePackagePart.getInputStream(), imageFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            String url = BaseFileUtil.getPathUnderUserFileServer(imageFile);
            
            if(vmlShape.ctShape.sizeOfClientDataArray()>0 && vmlShape.ctShape.getClientDataArray(0).sizeOfAnchorArray()>0){
            	Map<String,Object> setting = new HashMap<>();
            	setting.put("url",url);
	            
            	CTClientData ctClientData = vmlShape.ctShape.getClientDataArray(0);
	            String anchor = ctClientData.getAnchorList().get(0);
	            String[] anchorData =anchor.split(",");
	            setting.put("sr", Integer.parseInt(anchorData[2]));
	            setting.put("sc", Integer.parseInt(anchorData[0]));
	            setting.put("er", Integer.parseInt(anchorData[6]));
	            setting.put("ec", Integer.parseInt(anchorData[4]));
	            setting.put("sx", Integer.parseInt(anchorData[3]));
	            setting.put("sy", Integer.parseInt(anchorData[1]));
	            setting.put("ex", Integer.parseInt(anchorData[7]));
	            setting.put("ey", Integer.parseInt(anchorData[5]));
	            setting.put("floorType","picture");
	            return setting;
            }
    	}
    	return null;
    	
    }

    
    
    public static ReadEventSheetPicture build(SheetImportContext sheetImportContext){
        ReadEventSheetPicture picture = new ReadEventSheetPicture();
        XSSFReadEventSheet sheet = sheetImportContext.sheet();
        XSSFDrawing drawing = sheet.drawing();
        if(drawing != null){
            List<XSSFShape> shapes = drawing.getShapes();
            for(XSSFShape shape:shapes){
                if(shape instanceof XSSFPicture){
                    XSSFPicture pic = (XSSFPicture)shape;
                    try {
                        Map<String,Object> pictureSetting = handlePicture(pic,sheetImportContext);
                        if( pictureSetting != null )
                            picture._pictureSettings.add(pictureSetting);
                    } catch (IOException e) {
                        //ignore error
                    }
                }
            }
        }
        XSSFReadEventVMLDrawing vmlDrawing = sheet.vmlDrawing();
        if(vmlDrawing !=null){
        	List<CTShape> ctShapes = vmlDrawing.getShapes();
        	for(CTShape ctShape:ctShapes){
        		VMLShape vmlShape = new VMLShape(ctShape,vmlDrawing);
        		 try {
					Map<String,Object> pictureSetting =handleVMLShape(vmlShape,sheetImportContext);
					if(pictureSetting !=null){
						picture._pictureSettings.add(pictureSetting);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        }
        
        
        return picture;
    }
    
    
    private static class VMLShape{
    	private final CTShape ctShape;
    	private final XSSFReadEventVMLDrawing vmlDrawing;
    	
    	
    	public VMLShape(CTShape ctShape, XSSFReadEventVMLDrawing vmlDrawing) {
			super();
			this.ctShape = ctShape;
			this.vmlDrawing = vmlDrawing;
		}

		private PackagePart getVMLImageData() throws IOException{
			PackagePart imagePackagePart = null;
        	if(ctShape.sizeOfImagedataArray()>0){
        		String relId = ctShape.getImagedataArray(0).getRelid();
        		imagePackagePart =  vmlDrawing.getRelationById(relId).getPackagePart();
        	}
        	return imagePackagePart;
        }
		
		private String getId(){
			return ctShape.getId();
		}
    	
    }
    
    

}
