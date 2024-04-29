import java.util.*;

public class Slot {

	private char value;
	public ArrayList<Character> domain; //char[] domain;
	
	public Slot() {
		value = 'Z';
		domain = new ArrayList(0);
	}
	
	public void setValue(char v) {
		this.value = v;
	}
	public char getValue() {
		return value;
	}
	public void setDomain(char[] charArray) {
		for(int i = 0; i < charArray.length; i++) {
			domain.add(charArray[i]);
		}
	}
	public char[] getDomain() {
		char[] charArray = new char[domain.size()];
		for(int i = 0; i < domain.size(); i++) {
			charArray[i] = domain.get(i);
		}
		return charArray;
	}
}
