package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.DateUtil;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import static cn.wolfcode.p2p.base.util.UserContext.getVerifyCodeVo;


@Service
@Transactional
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Override
    public void sendVerifyCode(String phoneNumber) {
        //判断vo是否为null或者上一次发送验证码时间是否超过设定值
        VerifyCodeVo vo = getVerifyCodeVo();
        if (vo == null || DateUtil.getBetweenTime(new Date(), vo.getLastSendTime()) > 90) {
            String verifyCode = UUID.randomUUID().toString().substring(0, 4);
            StringBuilder sb = new StringBuilder(50);
            sb.append("【成都创信信息】验证码为：").append(verifyCode).append(",欢迎注册平台！");
            System.out.println(sb.toString());
            //执行短信发送,保存验证码信息到session中
            try{
                this.sendRealMessage(phoneNumber,sb.toString());
                vo = new VerifyCodeVo();
                vo.setLastSendTime(new Date());
                vo.setPhoneNumber(phoneNumber);
                vo.setVerifyCode(verifyCode);
                UserContext.setVerifyCodeVo(vo);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("验证码发送频繁,请稍后尝试");
        }
    }



    @Override
    public void sendRealMessage(String phoneNumber,String content) throws Exception{
        //http://utf8.api.smschinese.cn/?Uid=本站用户名&Key=接口安全秘钥&smsMob=手机号码&smsText=验证码:8888
        //https://way.jd.com/chuangxin/dxjk?mobile=13568813957&content=【成都创信信息】验证码为：5873,欢迎注册平台！&appkey=cded285740de6966fe424593d3e3e281
        //复制一个地址
        URL url = new URL("https://way.jd.com/chuangxin/dxjk");
        //打开浏览器,在地址栏输入地址
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        //设置请求方式
        connection.setRequestMethod("POST");
        //是否需要输入内容
        connection.setDoOutput(true);
        //给地址栏添加参数信息
        StringBuilder sb = new StringBuilder();
        sb.append("mobile=").append(phoneNumber).
                append("&content=").append(content).
                append("&appkey=").append("cded285740de6966fe424593d3e3e281");
        //输入数据
        connection.getOutputStream().write(sb.toString().getBytes("UTF-8"));
        //按下回车键
        connection.connect();
        //获取服务器响应的内容
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        System.out.println(response);

    }

    @Override
    public boolean check(String phoneNumber, String verifyCode) {
        VerifyCodeVo sessionVo = UserContext.getVerifyCodeVo();
        if (sessionVo != null&&
                phoneNumber.equals(sessionVo.getPhoneNumber())&&
                    verifyCode.equalsIgnoreCase(sessionVo.getVerifyCode())&&
                        DateUtil.getBetweenTime(new Date(),sessionVo.getLastSendTime())< BidConst.VERIFYCODE_VAILDATE_SECOND) {
                return true;
        }
        return false;
    }
}
