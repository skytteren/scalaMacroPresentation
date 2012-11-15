package macrofun

import scala.language.experimental.macros
import scala.reflect.macros.Context
import java.util.Locale
import java.util.ResourceBundle
import scala.reflect.runtime.{universe => ru}

object L {
	
	def applyImpl(c:Context)(bundle:c.Expr[Bundle], message:c.Expr[String], locale:c.Expr[Locale]):c.Expr[String] = {
		import c.universe._
		reify{
			val messageString:String = message.splice 
			val actualLocale:Locale = locale.splice
			val actualBundle:Bundle = bundle.splice
			assert(actualBundle.locales(actualLocale).getString(messageString) != null) 
			for{ locale <- actualBundle.locales.keys } yield {
				assert(actualBundle.locales(locale).getString(messageString) != null, "locale: " + locale) 
			}
			actualBundle.locales(actualLocale).getString(messageString)
		}
	}
	
	def apply(bundle:Bundle, message:String, locale:Locale):String = macro applyImpl
}

object Bundle{
	import scala.language.experimental.macros
	import scala.reflect.macros.Context
		
	def bundleImpl(c:Context)(name:c.Expr[String], locales:c.Expr[Set[Locale]]): c.Expr[Bundle] = {
		import c.universe._
		val Literal(Constant(nameString)) = name.tree
//		
//		val Apply(TypeApply(Select(Select(Select(This(scala), Predef), Set), apply), 
//				List(TypeTree())), List(Apply(Select(New(Select(Select(Ident(java), util), Locale)), <init>), 
//						List(Literal(Constant(NO)))), Select(Select(Select(Ident(java), util), Locale), FRENCH)))
		locales.tree match {
			case  Apply(_, List(localeSet)) => println("locales.set: " + ru.showRaw(localeSet))
			case _ => println("What")
		}
		
		val ref = c.universe.reify{	
			val nameString: String = name.splice
			val localesSet:Set[Locale] = locales.splice
			val localesTouples = for(locale <- localesSet) yield {
				val messages = ResourceBundle.getBundle(nameString, locale);
				assert(messages != null, "Can't find messageBundle for " + nameString + " locale: " + locale)
				locale -> messages
			}
			new Bundle(nameString, localesTouples.toMap)
		}
		println(ru.showRaw(ref))
		ref
	}
	
	def bundle(name:String, locales:Set[Locale]): Bundle = macro bundleImpl
}

class Bundle (val name:String, val locales:Map[Locale, ResourceBundle]) 

