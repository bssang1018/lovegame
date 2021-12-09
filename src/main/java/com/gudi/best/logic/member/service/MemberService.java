package com.gudi.best.logic.member.service;

import com.gudi.best.logic.member.mapper.MemberMapper;
import com.gudi.best.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Random;

@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    JavaMailSender sender;

    public String idCheck(String id) {
        return memberMapper.idCheck(id);
    }

    public boolean emailCheck(String email, HttpSession session) {
        boolean suc = false;
        if (EmailUtil.isValidEmail(email)) {
            // 난수
            Random random = new Random();
            String emailCheckNum = Integer.toString(random.nextInt(888888) + 111111);
            session.setAttribute("emailCheckNum", emailCheckNum);
            session.setAttribute("myEmail", email);
            // 이메일 보내기
            String setFrom = "qkrgks0147@gmail.com";
            String toMail = email;
            String title = "저희 Lovegame을 방문해주셔서 감사합니다 인증 코드 입니다";
            String content =
                    "<img src='https://hansol-bucket.s3.ap-northeast-2.amazonaws.com/imgs/20211007_115236.png'>" +
                            "<h1>홈페이지를 방문해주셔서 감사합니다</h1>" +
                            "<br>" +
                            "<h3>인증 번호는 " + emailCheckNum + "입니다.</h3>" +
                            "<h3>해당 인증번호를 인증번호 확인란에 정확하게 기입하여 주세요.</h3>";
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
                helper.setFrom(setFrom);
                helper.setTo(toMail);
                helper.setSubject(title);
                helper.setText(content, true);
                sender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            suc = true;
        }
        return suc;
    }

    public void join(HashMap<String, String> params) {
        // 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String enc_pass = encoder.encode(params.get("pw"));
        params.put("pw", enc_pass);
        memberMapper.join(params);
    }

    public String login(String id) {
        return memberMapper.login(id);
    }

    public String adminCheck(String id) {
        return memberMapper.adminCheck(id);
    }
}
