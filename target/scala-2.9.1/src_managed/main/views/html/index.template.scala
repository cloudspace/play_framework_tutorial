
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.api.templates.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import com.avaje.ebean._
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(Seq[Any](/*3.2*/main("Welcome to Play 2.0")/*3.29*/ {_display_(Seq[Any](format.raw/*3.31*/("""
    
    """),_display_(Seq[Any](/*5.6*/play20/*5.12*/.welcome(message, style = "Java"))),format.raw/*5.45*/("""
    
""")))})))}
    }
    
    def render(message:String) = apply(message)
    
    def f:((String) => play.api.templates.Html) = (message) => apply(message)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Jun 24 10:35:05 EDT 2013
                    SOURCE: /Users/jlorich/projects/play_framework_tutorial/app/views/index.scala.html
                    HASH: 40523c3560c255ffb9a20c98b65c17e640c1d57a
                    MATRIX: 755->1|849->18|886->21|921->48|960->50|1005->61|1019->67|1073->100
                    LINES: 27->1|30->1|32->3|32->3|32->3|34->5|34->5|34->5
                    -- GENERATED --
                */
            