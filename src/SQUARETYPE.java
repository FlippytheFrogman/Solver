public enum SQUARETYPE {
		TRIPLE_WORD("T"),
		DOUBLE_WORD("D"),
		TRIPLE_LETTER("t"),
		DOUBLE_LETTER("d"), 
		NORMAL_LETTER(" ");
		
		String mShortName;
	
		SQUARETYPE(String str) {
			mShortName = str;
		}
		
		public String toString() {
			return mShortName;
		}
}