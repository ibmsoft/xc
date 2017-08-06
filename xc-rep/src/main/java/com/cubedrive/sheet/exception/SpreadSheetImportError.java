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
package com.cubedrive.sheet.exception;

import com.cubedrive.base.exception.CubeDriveError;

@SuppressWarnings("serial")
public class SpreadSheetImportError extends CubeDriveError {

	public SpreadSheetImportError(String message) {
		super(message);
	}

}
