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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.STAxPos;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;

import com.cubedrive.sheet.service.sheet._export.handler.sheet.ChartProcessor.ChartInfo;

public class Charts {
    
    public static void buildChart(ChartInfo chartInfo,CTChart ctChart) throws Exception{
        String chartType = chartInfo.chartType();
        if(chartType != null && chartType.length() >0){
            Class<? extends AbstractChart> chartCls = null;
            switch(chartType){
                case "column": chartCls = ColumnChart.class; break;
                case "bar": chartCls = BarChart.class; break;
                case "line": chartCls = LineChart.class; break;
                case "scatter": chartCls = ScatterChart.class; break;
                case "area": chartCls = AreaChart.class; break;
                case "pie": chartCls = PieChart.class; break;
                case "radar": chartCls = RadarChart.class; break;
                default:
                    chartCls = null;
            }
            if (chartCls != null){
                Constructor<? extends AbstractChart> constructor = chartCls.getDeclaredConstructor(ChartInfo.class, CTChart.class);
                AbstractChart abstractChart =  constructor.newInstance(chartInfo, ctChart);
                abstractChart.build();
            }
        }
    }
	
	protected static abstract class AbstractChart{
		
		protected final ChartInfo chartInfo;
		protected final CTChart ctChart;
		protected final long catAxIdVal;
		protected final long valAxIdVal;
		
		protected STAxPos.Enum catAxPos;
		protected STAxPos.Enum valAxPos;
		
		protected String catTitle;
		protected String valTitle;
		
		protected AbstractChart(ChartInfo chartInfo,CTChart ctChart){
			this.chartInfo = chartInfo;
			this.ctChart = ctChart;
			this.catAxIdVal = new Random().nextInt(100000);
			this.valAxIdVal = catAxIdVal + 1;
			
			this.catTitle = chartInfo.xTitle();
			this.valTitle = chartInfo.yTitle();
		}
		
		protected final void build(){
		    ctChart.addNewAutoTitleDeleted().setVal(true);
			CTPlotArea ctPlotArea = ctChart.addNewPlotArea();
			ctPlotArea.addNewLayout();
			buildPlotArea(ctPlotArea);
			setLegend();
			ctChart.addNewPlotVisOnly().setVal(true);
			ctChart.addNewShowDLblsOverMax().setVal(false);
			setChart();
		}
		
		protected void buildPlotArea(CTPlotArea ctPlotArea){
			buildChart(ctPlotArea);
			addCatAx(ctPlotArea);
			addValAx(ctPlotArea);
			setPlotArea(ctPlotArea);
		}
		
		private void addCatAx(CTPlotArea ctPlotArea){
            CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
            ctCatAx.addNewAxId().setVal(catAxIdVal);
            ctCatAx.addNewScaling().addNewOrientation().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation.MIN_MAX);
            ctCatAx.addNewDelete().setVal(false);
            ctCatAx.addNewAxPos().setVal(catAxPos);
            if(catTitle !=null && catTitle.length()>0){
                CTTitle ctTitle = ctCatAx.addNewTitle();
                CTTextBody ctTextBody = ctTitle.addNewTx().addNewRich();
                ctTextBody.addNewBodyPr();
                ctTextBody.addNewLstStyle();
                CTTextParagraph ctTextParagraph = ctTextBody.addNewP();
                ctTextParagraph.addNewPPr().addNewDefRPr();
                CTRegularTextRun ctRegularTextRun = ctTextParagraph.addNewR();
                CTTextCharacterProperties ctTextCharacterProperties = ctRegularTextRun.addNewRPr();
                ctTextCharacterProperties.setAltLang("en-US");
                ctTextCharacterProperties.setLang("en-US");
                ctRegularTextRun.setT(catTitle);
                ctTitle.addNewLayout();
                ctTitle.addNewOverlay().setVal(false);
            }
            ctCatAx.addNewTickLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos.NEXT_TO);
            ctCatAx.addNewCrossAx().setVal(valAxIdVal);
            ctCatAx.addNewCrosses().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses.AUTO_ZERO);
            ctCatAx.addNewLblAlgn().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STLblAlgn.CTR);
            ctCatAx.addNewLblOffset().setVal(100);
            setCatAx(ctCatAx);
        }
        
        private void addValAx(CTPlotArea ctPlotArea){
            CTValAx ctValAx = ctPlotArea.addNewValAx();
            ctValAx.addNewAxId().setVal(valAxIdVal);
            ctValAx.addNewScaling().addNewOrientation().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation.MIN_MAX);
            ctValAx.addNewDelete().setVal(false);
            ctValAx.addNewAxPos().setVal(valAxPos);
            ctValAx.addNewMajorGridlines();
            if(valTitle != null && valTitle.length()>0){
                CTTitle ctTitle = ctValAx.addNewTitle();
                CTTextBody ctTextBody = ctTitle.addNewTx().addNewRich();
                ctTextBody.addNewBodyPr();
                ctTextBody.addNewLstStyle();
                CTTextParagraph ctTextParagraph = ctTextBody.addNewP();
                ctTextParagraph.addNewPPr().addNewDefRPr();
                CTRegularTextRun ctRegularTextRun = ctTextParagraph.addNewR();
                CTTextCharacterProperties ctTextCharacterProperties = ctRegularTextRun.addNewRPr();
                ctTextCharacterProperties.setAltLang("en-US");
                ctTextCharacterProperties.setLang("en-US");
                ctRegularTextRun.setT(valTitle);
                ctTitle.addNewLayout();
                ctTitle.addNewOverlay().setVal(false);
            }
            CTNumFmt ctNumFmt = ctValAx.addNewNumFmt();
            ctNumFmt.setFormatCode("General");
            ctNumFmt.setSourceLinked(true);
            ctValAx.addNewMajorTickMark().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark.OUT);
            ctValAx.addNewMinorTickMark().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark.NONE);
            ctValAx.addNewTickLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos.NEXT_TO);
            ctValAx.addNewCrossAx().setVal(catAxIdVal);
            ctValAx.addNewCrosses().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses.AUTO_ZERO);
            ctValAx.addNewCrossBetween().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STCrossBetween.BETWEEN);
            setValAx(ctValAx);
        }
		
		protected void setPlotArea(CTPlotArea ctPlotArea){
		    
		}
		
		protected void setChart(){
		    
		}
		
		private void setLegend(){
			String legendPos = chartInfo.legendPosition();
			org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.Enum pos;
			switch(legendPos){
				case "top":
					pos = org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.T;
					break;
				case "bottom":
					pos =  org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.B;
					break;
				case "left":
					pos =  org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.L;
					break;
				case "right":
				default:
					pos =  org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.R;
					break;
			}
			CTLegend ctLegend = ctChart.addNewLegend();
			ctLegend.addNewLegendPos().setVal(pos);
			ctLegend.addNewLayout();
			ctLegend.addNewOverlay().setVal(false);
		}
		
		protected abstract void buildChart(CTPlotArea ctPlotArea);
		
		protected void setCatAx(CTCatAx ctCatAx){
		}
		
		protected void setValAx(CTValAx ctValAx){
		}
	}
	
	protected static class BarChart extends AbstractChart{
		
		protected BarChart(ChartInfo chartInfo,CTChart ctChart){
			super(chartInfo,ctChart);
			this.catAxPos = STAxPos.L;
			this.valAxPos = STAxPos.B;
			this.catTitle = chartInfo.yTitle();
			this.valTitle = chartInfo.xTitle();
		}
		
		protected void setBarDir(CTBarChart ctBarChart){
			ctBarChart.addNewBarDir().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STBarDir.BAR);
		}

		@Override
		protected void buildChart(CTPlotArea ctPlotArea) {
			CTBarChart ctBarChart = ctPlotArea.addNewBarChart();
			setBarDir(ctBarChart);
			ctBarChart.addNewVaryColors().setVal(true);
			ctBarChart.addNewGrouping().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STBarGrouping.CLUSTERED);
			String sheetName = chartInfo.sheetName();
			List<CellReference> categoryList = chartInfo.categories();
			List<CellRangeAddress> valueList = chartInfo.series();
			CellRangeAddress label = chartInfo.labels();
			int seriesSize = valueList.size();
			
			for(int i=0; i< seriesSize;i++){
			    CellReference category =  categoryList.get(i);
				CellRangeAddress value = valueList.get(i);
				addBarSer(ctBarChart,category,value,label,i,sheetName);
			}
			ctBarChart.addNewAxId().setVal(catAxIdVal);
			ctBarChart.addNewAxId().setVal(valAxIdVal);
			
		}
		
		protected void addBarSer(CTBarChart ctBarChart, CellReference category,	CellRangeAddress value,
		        CellRangeAddress label,int index,String sheetName){
			org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer barSer = ctBarChart.addNewSer();
			barSer.addNewIdx().setVal(index);
			barSer.addNewOrder().setVal(index);
			if(category != null){
			    barSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
			}
			barSer.addNewInvertIfNegative().setVal(true);
			if(label != null){
			    barSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
			}
			barSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
		}
		
		protected void setCatAx(CTCatAx ctCatAx){
			ctCatAx.addNewAuto().setVal(true);
		}
	}
	
	protected static class ColumnChart extends BarChart{

		protected ColumnChart(ChartInfo chartInfo,CTChart ctChart) {
			super(chartInfo, ctChart);
			this.catAxPos = STAxPos.B;
			this.valAxPos = STAxPos.L;
		}
		
		protected void setBarDir(CTBarChart ctBarChart){
			ctBarChart.addNewBarDir().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STBarDir.COL);
		}
		
	}
	
	
	protected static class LineChart extends AbstractChart{

        protected LineChart(ChartInfo chartInfo,CTChart ctChart) {
            super(chartInfo, ctChart);
            catAxPos = STAxPos.B;
            valAxPos = STAxPos.L;
        }

        @Override
        protected void buildChart(CTPlotArea ctPlotArea) {
            CTLineChart ctLineChart = ctPlotArea.addNewLineChart();
            ctLineChart.addNewGrouping().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STGrouping.STANDARD);
            ctLineChart.addNewVaryColors().setVal(true);
            String sheetName = chartInfo.sheetName();
            List<CellReference> categoryList = chartInfo.categories();
            List<CellRangeAddress> valueList = chartInfo.series();
            CellRangeAddress label = chartInfo.labels();
            int seriesSize = valueList.size();
            
            for(int i=0; i< seriesSize;i++){
                CellReference category = categoryList.get(i);
                CellRangeAddress value = valueList.get(i);
                addLineSer(ctLineChart,category,value,label,i,sheetName);
            }
            ctLineChart.addNewAxId().setVal(catAxIdVal);
            ctLineChart.addNewAxId().setVal(valAxIdVal);
            
            
        }
        
        protected void addLineSer(CTLineChart ctLineChart, CellReference category,CellRangeAddress value,
                CellRangeAddress label,int index,String sheetName){
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer ctLineSer = ctLineChart.addNewSer();
            ctLineSer.addNewIdx().setVal(index);
            ctLineSer.addNewOrder().setVal(index);
            if(category != null){
                ctLineSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
            }
            ctLineSer.addNewMarker().addNewSymbol().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle.NONE);
            if(label != null){
                ctLineSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
            }
            ctLineSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
        }
        
        protected void setCatAx(CTCatAx ctCatAx){
            ctCatAx.addNewAuto().setVal(true);
        }
	    
	}
	
	protected static class ScatterChart extends LineChart{

        protected ScatterChart(ChartInfo chartInfo, CTChart ctChart) {
            super(chartInfo, ctChart);
        }
        
        protected void addLineSer(CTLineChart ctLineChart, CellReference category, CellRangeAddress value,
                CellRangeAddress label,int index,String sheetName){
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer ctLineSer = ctLineChart.addNewSer();
            ctLineSer.addNewIdx().setVal(index);
            ctLineSer.addNewOrder().setVal(index);
            if(category !=null){
                ctLineSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
            }
            ctLineSer.addNewSpPr().addNewLn().addNewNoFill();
            if(label != null){
                ctLineSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
            }
            ctLineSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
        }
	    
	}
	
	
	protected static class AreaChart extends AbstractChart{

        protected AreaChart(ChartInfo chartInfo,CTChart ctChart) {
            super(chartInfo,ctChart);
            catAxPos = STAxPos.B;
            valAxPos = STAxPos.L;
        }

        @Override
        protected void buildChart(CTPlotArea ctPlotArea) {
            CTAreaChart ctAreaChart = ctPlotArea.addNewAreaChart();
            ctAreaChart.addNewGrouping().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STGrouping.STACKED);
            ctAreaChart.addNewVaryColors().setVal(true);
            String sheetName = chartInfo.sheetName();
            List<CellReference> categoryList = chartInfo.categories();
            List<CellRangeAddress> valueList = chartInfo.series();
            CellRangeAddress label = chartInfo.labels();
            int seriesSize = valueList.size();
            
            for(int i=0; i< seriesSize;i++){
                CellReference category = categoryList.get(i);
                CellRangeAddress value = valueList.get(i);
                addAreaSer(ctAreaChart,category,value,label,i,sheetName);
            }
            ctAreaChart.addNewAxId().setVal(catAxIdVal);
            ctAreaChart.addNewAxId().setVal(valAxIdVal);
        }
        
        protected void addAreaSer(CTAreaChart ctAreaChart, CellReference category,CellRangeAddress value,
                CellRangeAddress label,int index,String sheetName) {
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaSer ctAreaSer = ctAreaChart.addNewSer();
            ctAreaSer.addNewIdx().setVal(index);
            ctAreaSer.addNewOrder().setVal(index);
            if(category != null){
                ctAreaSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
            }
            if(label != null){
                ctAreaSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
            }
            ctAreaSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
        }
	    
        protected void setCatAx(CTCatAx ctCatAx){
            ctCatAx.addNewAuto().setVal(true);
        }
        
	}
	
	protected static class RadarChart extends AbstractChart{

        protected RadarChart(ChartInfo chartInfo, CTChart ctChart) {
            super(chartInfo, ctChart);
            catAxPos = STAxPos.B;
            valAxPos = STAxPos.L;
            catTitle = null;
            valTitle = null;
        }

        @Override
        protected void buildChart(CTPlotArea ctPlotArea) {
            CTRadarChart ctRadarChart = ctPlotArea.addNewRadarChart();
            ctRadarChart.addNewRadarStyle().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STRadarStyle.FILLED);
            ctRadarChart.addNewVaryColors().setVal(true);
            String sheetName = chartInfo.sheetName();
            List<CellReference> categoryList = chartInfo.categories();
            List<CellRangeAddress> valueList = chartInfo.series();
            CellRangeAddress label = chartInfo.labels();
            int seriesSize = valueList.size();
            
            for(int i=0; i< seriesSize;i++){
                CellReference category = categoryList.get(i);
                CellRangeAddress value = valueList.get(i);
                addRadarSer(ctRadarChart,category,value,label,i,sheetName);
            }
            ctRadarChart.addNewAxId().setVal(catAxIdVal);
            ctRadarChart.addNewAxId().setVal(valAxIdVal);
            
        }
	    
        protected void addRadarSer(CTRadarChart ctRadarChart, CellReference category,CellRangeAddress value,
                CellRangeAddress label,int index,String sheetName) {
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer ctRadarSer = ctRadarChart.addNewSer();
            ctRadarSer.addNewIdx().setVal(index);
            ctRadarSer.addNewOrder().setVal(index);
            if(category != null){
                ctRadarSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
            }
            if(label != null){
                ctRadarSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
            }
            ctRadarSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
        }
	    
        protected void setCatAx(CTCatAx ctCatAx){
            ctCatAx.addNewMajorGridlines();
            ctCatAx.addNewAuto().setVal(true);
        }
	    
	}
	
	protected static class PieChart extends AbstractChart{

        protected PieChart(ChartInfo chartInfo, CTChart ctChart) {
            super(chartInfo, ctChart);
        }
        
        protected void buildPlotArea(CTPlotArea ctPlotArea){
            buildChart(ctPlotArea);
            setPlotArea(ctPlotArea);
        }

        @Override
        protected void buildChart(CTPlotArea ctPlotArea) {
            CTPieChart ctPieChart = ctPlotArea.addNewPieChart();
            ctPieChart.addNewVaryColors().setVal(true);
            String sheetName = chartInfo.sheetName();
            List<CellReference> categoryList = chartInfo.categories();
            List<CellRangeAddress> valueList = chartInfo.series();
            CellRangeAddress label = chartInfo.labels();
            int seriesSize= valueList.size();
            
            for(int i=0; i< seriesSize; i++){
                CellReference category = categoryList.get(i);
                CellRangeAddress value = valueList.get(i);
                addPieSer(ctPieChart,category,value,label,i,sheetName);
            }
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls ctDLbls = ctPieChart.addNewDLbls();
            ctDLbls.addNewShowVal().setVal(true);
            ctPieChart.addNewFirstSliceAng().setVal(0);
        }
        
        private void addPieSer(CTPieChart ctPieChart, CellReference category,
            CellRangeAddress value,CellRangeAddress label,int index,String sheetName){
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer ctPieSer = ctPieChart.addNewSer();
            ctPieSer.addNewIdx().setVal(index);
            ctPieSer.addNewOrder().setVal(index);
            if(category != null){
                ctPieSer.addNewTx().addNewStrRef().setF(formatCellReference(sheetName,category));
            }
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls ctDLbls = ctPieSer.addNewDLbls();;
            ctDLbls.addNewDLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.BEST_FIT);
            ctDLbls.addNewShowCatName().setVal(true);
            ctDLbls.addNewShowLeaderLines().setVal(true);
            if(label != null){
                ctPieSer.addNewCat().addNewStrRef().setF(formatCellRangeAddress(sheetName,label));
            }
            ctPieSer.addNewVal().addNewNumRef().setF(formatCellRangeAddress(sheetName,value));
        }
        
        protected void setChart(){
            ctChart.addNewDispBlanksAs().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STDispBlanksAs.ZERO);;
        }
	    
	}
	
	
	private static String formatCellRangeAddress(String sheetName,CellRangeAddress cellRangeAddress){
		StringBuilder buf = new StringBuilder();
		buf.append(sheetName);
		buf.append("!");
		buf.append("$").append(CellReference.convertNumToColString(cellRangeAddress.getFirstColumn()));
		buf.append("$").append(cellRangeAddress.getFirstRow()+1);
		if(!(cellRangeAddress.getFirstRow()==cellRangeAddress.getLastRow() && cellRangeAddress.getFirstColumn() == cellRangeAddress.getLastColumn())){
    		buf.append(":");
    		buf.append("$").append(CellReference.convertNumToColString(cellRangeAddress.getLastColumn()));
    		buf.append("$").append(cellRangeAddress.getLastRow()+1);
		}
		return buf.toString();
	}
	
	private static String formatCellReference(String sheetName,CellReference cellReference){
		StringBuilder buf = new StringBuilder();
		buf.append(sheetName);
		buf.append("!");
		buf.append("$").append(CellReference.convertNumToColString(cellReference.getCol()));
		buf.append("$").append(cellReference.getRow()+1);
		return buf.toString();
	}
}
