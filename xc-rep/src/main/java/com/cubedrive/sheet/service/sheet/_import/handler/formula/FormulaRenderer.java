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
package com.cubedrive.sheet.service.sheet._import.handler.formula;

import java.util.Stack;

import com.cubedrive.sheet.service.sheet._import.handler.formula.ParseNode.PtgState;



class FormulaRenderer {

    static String render(PtgState[] rpnStates) {
        Stack<String> stack = new Stack<>();
        for(PtgState ptgState:rpnStates){
            Ptg ptg = ptgState.getPtg();
            int count = ptgState.getSubCount();
            String ptgString;
            if(count == 0){
                ptgString =ptg.stringValue();
            }
            else{
                String[] subStrings = new String[count];
                for(int i=0;i<count;i++){
                    subStrings[count-i-1] = stack.pop();
                }
                ptgString =ptg.stringValue(subStrings);
            }
            stack.push(ptgString);
        }
        return stack.pop();
    }
    

//    public static void main(String []args){
//        String formulaString = "sum(A1,A2,A3)+sum(A1:B1,name(1,2,3)+(1))";
//        String formulaString = "IF(A1=B1,\"Ok\",\"not ok\")";
//        ParseNode rootNode = FormulaParser.parse(formulaString, null);
//        PtgState[] rpnArray = ParseNode.toRPN(rootNode);
//        System.out.println(renderSharedFormula(formulaString,null,1,0));
//    }
    
}
