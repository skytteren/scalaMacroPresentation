package macrofun

import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.util.matching.Regex

object RegExp {

	def regexpImp(c:Context)(regexString:c.Expr[String]): c.Expr[Regex] = {
		import c.universe._
		regexString.tree match {
      case Literal(Constant(string: String)) =>
        string.r // just to check
        c.universe.reify(regexString.splice.r)
    }
	}
	
	def regexp(regexString:String):Regex = macro regexpImp
	
}

