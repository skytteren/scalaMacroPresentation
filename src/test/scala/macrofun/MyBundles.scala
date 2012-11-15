package macrofun

import Bundle._
import java.util.Locale
import org.scalatest.FunSpec

object MyBundles{
	val application = bundle("macrofun.ApplicationMessage", Set(new Locale("NO"), Locale.ENGLISH))
	val applicationFail = bundle("macrofun.ApplicationMessage_tull", Set(new Locale("NO"), Locale.FRENCH))
	val applicationFail2 = bundle("macrofun.ApplicationMessage", Set(new Locale("NO"), Locale.FRENCH))
	println(applicationFail)
	println(applicationFail2)
}

class MyBundleSpec extends FunSpec{
	import MyBundles._
	describe("Bundle"){
		it("should print hello world"){
			assert(L(application, "macrofun.application.hello.world", new Locale("NO")) === "Hallo verden!")
			assert(L(application, "macrofun.application.hello.world", Locale.ENGLISH) === "Hello world!")
			assert(L(applicationFail, "macrofun.application.hello.world", Locale.ENGLISH) === "Hello world!")
		}
		
	}
	
}