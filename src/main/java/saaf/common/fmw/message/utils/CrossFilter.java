package saaf.common.fmw.message.utils;

import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;


public class CrossFilter implements Filter {
    private FilterConfig filterConfig = null;

    public CrossFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("CrossFilter()");
        }
        StringBuffer sb = new StringBuffer("CrossFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse alteredResponse = ((HttpServletResponse)response);
            // I need to find a way to make sure this only gets called on 200-300 http responses
            // TODO: see above comment
            addHeadersFor200Response(alteredResponse);
        }
        ((HttpServletResponse)response).setHeader("Access-Control-Allow-origin", "*");
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, XRequested-With, Content-Type, Accept, TokenCode");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        doBeforeProcessing(request, response);

        //验证Token参数
        HttpServletRequest request_ = (HttpServletRequest)request;
        try {
            String tokenCode = request_.getHeader("TokenCode");
            String tokenKey = request_.getHeader("TokenKey");
            //            request_.getSession().getId()
            //            String exptTokenCode = SToolUtils.object2String(request_.getParameterMap().get("TokenCode")[0]);
            //            String exptTokenKey = SToolUtils.object2String(request_.getParameterMap().get("TokenKey"));
            String s1 = request_.getParameter("TokenCode");
            if (null != request_.getParameter("TokenCode") && !"".equals(request_.getParameter("TokenCode"))) {
                tokenCode = request_.getParameter("TokenCode");
                tokenKey = request_.getParameter("TokenKey");
            }
            String notCheck = "upload,downloadFile,login,pay";
            String reqPathInfo = request_.getPathInfo();
            reqPathInfo =(null==reqPathInfo?"": reqPathInfo.substring(reqPathInfo.lastIndexOf("/") + 1));
            if (!notCheck.contains(reqPathInfo)) {
                //if (!request_.getPathInfo().contains("file") && !request_.getPathInfo().contains("downloadFile")) {
                //外部平台和调度使用TokenKey 验证Token机制
                if (null != tokenKey && !"".equals(tokenKey)) {
                    checkToken(tokenKey, tokenCode);
                } else {
                    //内部平台验证Token机制，跳过后台登录验证 saafLoginServlet
                    if (!request_.getRequestURL().toString().contains("saafLoginServlet")) {
//                        checkToken(request_.getSession().getId(), tokenCode);
                        //mdf by zl 2017-05-04
                        checkToken(tokenCode,"UserId");
//                        throw new NotTokenException();
                    }
                }
            }

        } catch (NotTokenException e) {

            HttpServletResponse response_ = (HttpServletResponse)response;
            JSONObject json = new JSONObject();
            json.put("status", "NOTOKEN");
            json.put("msg", e.getMessage());
            json.put("count", 0);
            json.put("data", "");
            PrintWriter servletout = response_.getWriter();
            servletout.write(json.toString());
            servletout.flush();
            servletout.close();
            return;
        }
        //验证Token参数 完成
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException)problem;
            }
            if (problem instanceof IOException) {
                throw (IOException)problem;
            }
            sendProcessingError(problem, response);
        }


    }

    /**
     * 验证Token是否失效
     * @param tokenCode
     * @throws NotTokenException
     */
    public void checkToken(String tokenCode, String keword) throws NotTokenException {
//        boolean isValidToken = false;
//        //请求验证Token  NOTOKEN
//        if (null != tokenCode && !"".equals(tokenCode)) {
//            WSSecurityPolicy wsSecurityPolicy = (WSSecurityPolicy)SaafToolUtils.context.getBean("wsSecurityPolicy");
//            isValidToken = wsSecurityPolicy.isValidToken(sessionId, tokenCode);
//            if (!isValidToken) {
//                throw new NotTokenException("在请求失败，Token已过期!");
//            }
//        } else {
//            throw new NotTokenException("在请求失败，TokenCode错误!");
//        }

        //mdf by zl 2017-05-04
//        boolean isValidToken = false;
//        //请求验证Token  NOTOKEN
//        if (null != tokenCode && !"".equals(tokenCode)) {
//            WSSecurityPolicy wsSecurityPolicy = (WSSecurityPolicy)SaafToolUtils.context.getBean("wsSecurityPolicy");
//            isValidToken = wsSecurityPolicy.isValidToken(tokenCode, keword);
//            if (!isValidToken) {
//                throw new NotTokenException("在请求失败，Token已过期!");
//            }
//        } else {
//            throw new NotTokenException("在请求失败，TokenCode错误!");
//        }

    }

    @Override
    public void destroy() {

    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        log("CrossFilter:DoBeforeProcessing");

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        log("CrossFilter:DoAfterProcessing");
    }

    private void addHeadersFor200Response(HttpServletResponse response) {
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }


}
