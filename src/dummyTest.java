/**
 * Created by Mahmoud on 03,2020
 */
public class dummyTest {
	public static void main(String[] args) {
		String msg = "Mahmoud Almahdi und another one .    and more ";
		String[] tokens = msg.split(" ", 4);

		for (String ele : tokens) {
			System.out.println(ele);
		}
	}
}
