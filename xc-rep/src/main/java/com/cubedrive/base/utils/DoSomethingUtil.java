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
package com.cubedrive.base.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DoSomethingUtil {
    
    private static final Logger LOG = LoggerFactory.getLogger(DoSomethingUtil.class);
    
    public static void doSomething(DoSomething...doSomethings ){
        DoSomethingHandler handler = new DoSomethingHandler();
        for(DoSomething doSomething:doSomethings){
            handler._doSomethings.add(doSomething);
        }
        handler.handle();
        
    }
    
    public enum OnException{
        abort,ignore,raise
    }
    
    public static interface DoSomething{
        public void doIt();
        public OnException onException();
    }
    
    private static class DoSomethingHandler{
        private final List<DoSomething> _doSomethings = new ArrayList<>();
        
        private DoSomethingHandler(){
        }
        
        private void handle(){
            for(DoSomething doSomething:_doSomethings){
                try{
                    doSomething.doIt();
                }catch(Exception ex){
                    LOG.error("", ex);
                    OnException _onException = doSomething.onException();
                    if(_onException == OnException.abort)
                        break;
                    else if(_onException == OnException.ignore)
                        continue;
                    else
                        throw ex;
                }
            }
        }
    }
    
    public static void main(String[]args){
        DoSomething doSomething1 = new DoSomething(){
            @Override
            public void doIt() {
                System.out.println("doSomething1 doIt");
            }
            public OnException onException(){
                return OnException.ignore;
            }
            
        };
        DoSomething doSomething2 = new DoSomething(){
            @Override
            public void doIt() {
                System.out.println("doSomething2 doIt");
                System.out.println(1/0);
            }
            public OnException onException(){
                return OnException.abort;
            }
            
        };
        DoSomething doSomething3 = new DoSomething(){
            @Override
            public void doIt() {
                System.out.println("doSomething3 doIt");
            }
            public OnException onException(){
                return OnException.ignore;
            }
            
        };
        
        doSomething(doSomething1,doSomething2,doSomething3);
    }
    

}
