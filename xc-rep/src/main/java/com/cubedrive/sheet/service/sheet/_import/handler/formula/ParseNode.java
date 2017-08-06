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

import java.util.ArrayList;
import java.util.List;



final class ParseNode {

    public static final ParseNode[] EMPTY_ARRAY = {};
    private Ptg _token;
    private ParseNode[] _children;
    

    ParseNode(Ptg token, ParseNode[] children) {
        if (token == null)
            throw new IllegalArgumentException("token must not be null");
        _token = token;
        _children = children;

    }


    ParseNode(Ptg token) {
        this(token, EMPTY_ARRAY);
    }

    ParseNode(Ptg token, ParseNode child0) {
        this(token, new ParseNode[]{child0,});
    }

    ParseNode(Ptg token, ParseNode child0, ParseNode child1) {
        this(token, new ParseNode[]{child0, child1});
    }


    public Ptg getToken() {
        return _token;
    }

    void setToken(Ptg token) {
        _token = token;
    }

    public ParseNode[] getChildren() {
        return _children;
    }

    void setChildren(ParseNode[] children) {
        _children = children;
    }
    
    private void collectPtgs(TokenCollector collector){
        for(int i=0; i<_children.length; i++){
            _children[i].collectPtgs(collector);
        }
        collector.add(new PtgState(_token,_children.length));
    }
    
    static PtgState[] toRPN(ParseNode rootNode){
        TokenCollector tokenCollector = new TokenCollector();
        rootNode.collectPtgs(tokenCollector);
        return tokenCollector.getResult();
    }
    
 
    
    static final class PtgState {
        private Ptg ptg;
        private final int subCount;
        
        private PtgState(Ptg ptg,int subCount){
            this.ptg = ptg;
            this.subCount =subCount;
        }
        
        Ptg getPtg(){
            return this.ptg;
        }
        
        void setPtg(Ptg ptg){
            this.ptg = ptg;
        }
        
        int getSubCount(){
            return this.subCount;
        }
        
    }
    
    
    private static final class TokenCollector {
        private final List<PtgState> _ptgs =new ArrayList<>();
        
        private TokenCollector(){
        }
        
        private  void add(PtgState ptgState){
            _ptgs.add(ptgState);
        }
        
        public PtgState[] getResult(){
            return _ptgs.toArray(new PtgState[0]);
        }
        
        
    }


}
