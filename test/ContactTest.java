import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.dt.bean.Contact;


public class ContactTest {

	private Contact[] contacts;
	
	@Before
	public void setup(){
		contacts = new Contact[]{
			new Contact("1","1","ÂÀÏè","15250991986"),
			new Contact("2","2","Â½âùÆ½","15298387110"),
			new Contact("3","3","13337812390","13337812390")
		};
	}
	
	@Test
	public void sortTest(){
		Arrays.sort(contacts);
		assertEquals(contacts[0],new Contact("3","3","13337812390","13337812390"));
	}
	

}
