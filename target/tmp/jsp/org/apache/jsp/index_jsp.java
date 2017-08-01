package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<body>\n");
      out.write("<h2>本地头条数据抓取页面</h2>\n");
      out.write("<form action=\"index/crawBendiNewsData\" method=\"post\" ><br>\n");
      out.write("列表类型：\n");
      out.write("<span style=\"font-size:18px;\"><select id=\"type\" name=\"type\">  \n");
      out.write("    <option value=\"1\">默认列表</option>  \n");
      out.write("    <option value=\"2\">手动下拉列表</option>  \n");
      out.write("</select> </span>\n");
      out.write("\n");
      out.write("<br>\n");
      out.write("参数：<input type=\"text\"  style=\"width:500px;height:50px\" name=\"params\" ><br>\n");
      out.write("<input type=\"submit\" value=\"提交\" >\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("<br><br>\n");
      out.write("<h2>大众点评数据抓取页面</h2>\n");
      out.write("<form action=\"index/crawDianpingShopsData\" method=\"post\" >\n");
      out.write("抓取城市：<input type=\"text\"  style=\"width:100px;\" name=\"cityName\" ><br>\n");
      out.write("抓取类型：\n");
      out.write("<span style=\"font-size:18px;\"><select id=\"type\" name=\"type\">  \n");
      out.write("    <option value=\"FOOD\">美食</option>  \n");
      out.write("    <option value=\"VIDEO\">电影</option>  \n");
      out.write("    <option value=\"LIFE\">休闲娱乐</option>  \n");
      out.write("    <option value=\"HOTEL\">酒店</option>  \n");
      out.write("    <option value=\"BEAUTY\">丽人</option>  \n");
      out.write("    <option value=\"KTV\">K歌</option>  \n");
      out.write("    <option value=\"SPORT\">运动健身</option>  \n");
      out.write("    <option value=\"PLAY\">周边游</option>  \n");
      out.write("    <option value=\"BABY\">亲自</option>  \n");
      out.write("    <option value=\"MARRIED\">结婚</option>  \n");
      out.write("    <option value=\"SHOP\">购物</option>  \n");
      out.write("    <option value=\"PET\">宠物</option>  \n");
      out.write("    <option value=\"LIFESERVICE\">生活服务</option>  \n");
      out.write("    <option value=\"TRAIN\">学习培训</option>  \n");
      out.write("    <option value=\"CAR\">爱车</option>  \n");
      out.write("    <option value=\"HEALTH\">医疗健康</option>  \n");
      out.write("    <option value=\"HOME\">家装</option>  \n");
      out.write("    <option value=\"FEAST\">宴会</option>  \n");
      out.write("</select> </span>\n");
      out.write("<br>\n");
      out.write("<input type=\"submit\" value=\"提交\" >\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("<script type=\"text/javascript\" src=\"decode.js\"></script>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
