/**
 * CubeDrive Private Cloud
 * Copyright (c) FeyaSoft Inc 2014. All right reserved.
 * http://www.cubedrive.com
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
package com.cubedrive.base.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.persistence.common.SeqMapper;

@Service
public class SeqService {
	
	@Autowired
	private SeqMapper seqMapper;
	
	private static final Object _lock = new Object();
	
	public Integer getUUID(String seqName) {
		int uuid = seqMapper.getNextvalBySeq(seqName);
		if(uuid == 0){
			uuid = createAndGetUUID(seqName);
		}
		return uuid;
		
	}
	
	private int createAndGetUUID(String seqName){
		int uuid;
		synchronized(_lock){
			uuid = seqMapper.getNextvalBySeq(seqName);
			if(uuid == 0){
				seqMapper.insertSequence(seqName);
				uuid = seqMapper.getNextvalBySeq(seqName);
			}
		}
		return uuid;
	}
	
	
}
