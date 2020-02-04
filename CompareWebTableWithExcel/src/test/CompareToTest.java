package test;

public class CompareToTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1="1000.00";
		String s2="55.55";
		System.out.println(s1.compareTo(s2));
		System.out.println(s2.compareTo(s1));
		System.out.println(s2.compareTo(s2));
		System.out.println("Check Double");
		Double d1=new Double(1000.00);
		Double d2=new Double(55.55);
		System.out.println(d1.compareTo(d2));
		//System.out.println(d1.compareTo(s1));
	}

}
