/**
 * EnterpriseSheet - online spreadsheet solution
 * Copyright (c) FeyaSoft Inc 2014. All right reserved.
 * http://www.enterpriseSheet.com
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.xzsoft.xc.rep.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtil {

	private static final ObjectMapper jsonMapper = new ObjectMapper();

	private JsonUtil() {

	}
	
	public static JsonGenerator  jsonGenerator(OutputStream output) throws IOException{
		JsonGenerator jGenerator = jsonMapper.getJsonFactory().createJsonGenerator(output);
		return jGenerator;
	}
	
	public static JsonGenerator jonsGenerator(Writer writer) throws IOException{
		JsonGenerator jGenerator = jsonMapper.getJsonFactory().createJsonGenerator(writer);
		return jGenerator;
	}

	public static <T> byte[] toJsonByteArray(T bean){
		ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
		toJson(bean,baos);
		return baos.toByteArray();
	}
	
	
	public static <T> String toJson(T bean) {
		CompatibleJavascriptJsonStringWriter writer = 
				new CompatibleJavascriptJsonStringWriter();
        toJson(bean,writer);
        return writer.toString();
	}

    public static <T> void toJson(T bean,OutputStream outputStream){
        //toJson(bean,new OutputStreamWriter(outputStream,ApplicationProperties.CHARSET_UTF8));
        try {
			toJson(bean,new OutputStreamWriter(outputStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

	public static <T> void toJson(T bean,Writer writer){
		try{
            JsonGenerator jsonGenerator = jsonMapper.getJsonFactory().createJsonGenerator(writer);
            jsonMapper.writeValue(jsonGenerator, bean);
        }catch(IOException e){
            e.printStackTrace();
        }
	}

	public static <T> T fromJson(String json, Class<T> valueType) {
        return fromJson(new StringReader(json),valueType);
	}

    public static <T> T fromJson(InputStream inputStream,Class<T> valueType) {
        //return fromJson(new InputStreamReader(inputStream,ApplicationProperties.CHARSET_UTF8),valueType);
    	return null;
    }

    public static  <T> T fromJson(byte[] bytes,Class<T> valueType) {
        //return fromJson(new InputStreamReader(new ByteArrayInputStream(bytes),ApplicationProperties.CHARSET_UTF8),valueType);
    	return null;
    }

    public static <T> T fromJson(Reader reader,Class<T>  valueType) {
        try {
            return jsonMapper.readValue(reader, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

    public static <T> T fromJson(InputStream inputStream,TypeReference<T> typeReference){
        //return fromJson(new InputStreamReader(inputStream,ApplicationProperties.CHARSET_UTF8),typeReference);
    	return null;
    }

    public static <T> T fromJson(String s,TypeReference<T> typeReference){
        return fromJson(new StringReader(s),typeReference);
    }

    public static <T> T fromJson(byte[] bytes,TypeReference<T> typeReference){
        //return fromJson(new InputStreamReader(new ByteArrayInputStream(bytes),ApplicationProperties.CHARSET_UTF8),typeReference);
    	try {
			return fromJson(new InputStreamReader(new ByteArrayInputStream(bytes),"UTF-8"),typeReference);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    public static <T> T fromJson(Reader reader,TypeReference<T> typeReference){
        try {
            return jsonMapper.readValue(reader, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getJsonObj(String json) {
		return (Map<String, Object>)fromJson(json,Map.class);
	}

	@SuppressWarnings("unchecked")
	public static List<Object> parseArray(String jsonString) {
		return fromJson(jsonString,new TypeReference<List<Object>>() {});
	}
	
	public static <T> List<T> parseArray(String jsonString, Class<?> clazz) {
		try{
			return jsonMapper.readValue(jsonString, jsonMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		}catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	private static class CompatibleJavascriptJsonStringWriter extends Writer{
		
		private StringBuilder buf;
		 
		private CompatibleJavascriptJsonStringWriter(){
			 buf = new StringBuilder();
	         lock = buf;
		}
		 
		private CompatibleJavascriptJsonStringWriter(int initialSize) {
	        if (initialSize < 0) {
	            throw new IllegalArgumentException("Negative buffer size");
	        }
	        buf = new StringBuilder(initialSize);
	        lock = buf;
	    }
		
		public void write(int c) {
			if(c == 8232) //escap  \u2028 - Line separator
				buf.append("\\u2028");
			else if(c == 8233) // escape \u2029 - Paragraph separator
				buf.append("\\u2029");
			else
				buf.append((char)c);
	    }
		
		public void write(char cbuf[], int off, int len) {
	        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
	            ((off + len) > cbuf.length) || ((off + len) < 0)) {
	            throw new IndexOutOfBoundsException();
	        } else if (len == 0) {
	            return;
	        }
	        for(int i=0;i<len;i++){
	        	int c = cbuf[off+i];
	        	write(c);
	        }
	        
		}
		
		public void write(String str) {
			for(int i=0;i<str.length();i++){
				int c = str.charAt(i);
				write(c);
			}
	    }
		
		public void write(String str, int off, int len)  {
			for(int i=0;i<len;i++){
				int c= str.charAt(off + i);
				write(c);
			}
		}
		
		public CompatibleJavascriptJsonStringWriter append(CharSequence csq) {
	        if (csq == null)
	            write("null");
	        else
	            write(csq.toString());
	        return this;
	    }
		
		public CompatibleJavascriptJsonStringWriter append(CharSequence csq, int start, int end) {
	        CharSequence cs = (csq == null ? "null" : csq);
	        write(cs.subSequence(start, end).toString());
	        return this;
	    }
		
		public CompatibleJavascriptJsonStringWriter append(char c) {
	        write(c);
	        return this;
	    }
		
		public String toString() {
		        return buf.toString();
	    }
		
		public void flush() {}
		
		public void close() throws IOException {}
		
	}
	

}
