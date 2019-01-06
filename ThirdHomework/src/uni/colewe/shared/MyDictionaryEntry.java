package uni.colewe.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MyDictionaryEntry implements IsSerializable{

	private String rus;
	private String pos;
	private String eng;
	
	public MyDictionaryEntry(){
		setRus("");
		setPos("");
		setEng("");
	}
	
	public MyDictionaryEntry(String rus, String pos, String eng){
		setRus(rus);
		setPos(pos);
		setEng(eng);
	}
	
	public String getRus() {
		return rus;
	}
	public void setRus(String rus) {
		this.rus = rus;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getEng() {
		return eng;
	}
	public void setEng(String eng) {
		this.eng = eng;
	}
	
	@Override
	public String toString(){
		return rus + "\t"+ pos + "\t" + eng;
	}
}
