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

import java.util.Objects;


class ConditionalFormattingIcons {
	
	static final ConditionalFormattingIcon dummy_icon = new ConditionalFormattingIcon(-1,-1);
	
	static ConditionalFormattingIcon getIconByName(String iconSetName){
		int set,level;
		switch(iconSetName){
			case "3Arrows":
				set = 0; level = 3;break;
			case "3Symbols2":
				set = 1; level = 3; break;
			case "3Symbols":
				set = 2; level = 3; break;
			case "3Flags":
				set = 5; level = 3; break;
			case "3ArrowsGray":
				set = 6; level =3; break;
			case "4Rating":
				set = 7; level =3; break;
			case "3TrafficLights2":
				set = 9; level =3; break;
			case "4RedToBlack":
				set = 3; level = 4; break;
			case "4TrafficLights":
				set = 4; level = 4; break;
			default:
				set = -1; level = -1;break;
				
		}
		if (set == -1 && level == -1)
			return dummy_icon;
		else
			return new ConditionalFormattingIcon(set,level);
	}
	
	
	static class ConditionalFormattingIcon {
		private final int _set;
		private final int _level;
		
		private ConditionalFormattingIcon(int set, int level){
			_set = set;
			_level = level;
		}
		
		int getSet(){
			return _set;
		}
		
		int getLevel(){
			return _level;
		}
		
		public boolean equals(Object o){
			if(o == this)return true;
			if(!(o instanceof ConditionalFormattingIcon))return false;
			ConditionalFormattingIcon other = (ConditionalFormattingIcon)o;
			return Objects.equals(_set, other._set) && Objects.equals(_level, other._level);
		}
		
		public int hashCode(){
			return Objects.hash(_set, _level);
		}
	}

}
