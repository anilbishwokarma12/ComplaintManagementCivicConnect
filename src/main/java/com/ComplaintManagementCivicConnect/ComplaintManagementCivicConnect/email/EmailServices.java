package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServices {

    private  final JavaMailSender mailer;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendVerificationEmail(String to, String name, String token) {
        String link = baseUrl + "/api/auth/verify-email?token=" + token;
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;
                        border:1px solid #eee;border-radius:10px;overflow:hidden;">
              <div style="background:#0F3460;padding:28px 32px;">
                <h1 style="color:#fff;margin:0;font-size:22px;">🏛 CivicConnect</h1>
                <p style="color:rgba(255,255,255,.6);margin:4px 0 0;font-size:13px;">
                  AI-Powered Civic Governance
                </p>
              </div>
              <div style="padding:36px 32px;background:#fafafa;">
                <h2 style="color:#0F3460;margin-top:0;">Hi %s, welcome!</h2>
                <p style="color:#555;line-height:1.7;">
                  You're almost there. Click the button below to verify your
                  email address and activate your CivicConnect account.
                </p>
                <div style="text-align:center;margin:32px 0;">
                  <a href="%s"
                     style="background:#0F3460;color:#fff;padding:14px 32px;
                            border-radius:8px;text-decoration:none;
                            font-weight:bold;font-size:15px;display:inline-block;">
                    ✅ Verify My Email
                  </a>
                </div>
                <p style="color:#aaa;font-size:12px;">
                  This link expires in <b>24 hours</b>.
                  If you didn't register, just ignore this email.
                </p>
              </div>
              <div style="background:#f0f0f0;padding:16px 32px;text-align:center;">
                <p style="color:#bbb;font-size:11px;margin:0;">
                  © 2025 CivicConnect. All rights reserved.
                </p>
              </div>
            </div>
            """.formatted(name, link);

        send(to, "Verify your CivicConnect account", html);
    }


    // ── Password Reset ────────────────────────────────────────────────────────

    @Async
    public void sendPasswordResetEmail(String to, String name, String token) {
        String link = baseUrl + "/api/auth/reset-password?token=" + token;
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;
                        border:1px solid #eee;border-radius:10px;overflow:hidden;">
              <div style="background:#0F3460;padding:28px 32px;">
                <h1 style="color:#fff;margin:0;font-size:22px;">🏛 CivicConnect</h1>
              </div>
              <div style="padding:36px 32px;background:#fafafa;">
                <h2 style="color:#c0392b;margin-top:0;">🔑 Password Reset</h2>
                <p style="color:#555;line-height:1.7;">
                  Hello <b>%s</b>, we received a request to reset your password.
                  Click below — this link expires in <b>1 hour</b>.
                </p>
                <div style="text-align:center;margin:32px 0;">
                  <a href="%s"
                     style="background:#c0392b;color:#fff;padding:14px 32px;
                            border-radius:8px;text-decoration:none;
                            font-weight:bold;font-size:15px;display:inline-block;">
                    🔒 Reset My Password
                  </a>
                </div>
                <p style="color:#aaa;font-size:12px;">
                  If you didn't request this, your account is safe — just ignore this email.
                </p>
              </div>
            </div>
            """.formatted(name, link);

        send(to, "Reset your CivicConnect password", html);
    }


    // ── Password Changed Confirmation ─────────────────────────────────────────

    @Async
    public void sendPasswordChangedEmail(String to, String name) {
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;
                        border:1px solid #eee;border-radius:10px;overflow:hidden;">
              <div style="background:#0F3460;padding:28px 32px;">
                <h1 style="color:#fff;margin:0;font-size:22px;">🏛 CivicConnect</h1>
              </div>
              <div style="padding:36px 32px;background:#fafafa;">
                <h2 style="color:#27ae60;margin-top:0;">✅ Password Changed</h2>
                <p style="color:#555;line-height:1.7;">
                  Hello <b>%s</b>, your password was changed successfully.
                </p>
                <p style="color:#555;">
                  If you did not make this change, please contact support immediately
                  or use <b>Forgot Password</b> to secure your account.
                </p>
              </div>
            </div>
            """.formatted(name);

        send(to, "Your CivicConnect password was changed", html);
    }



    // ── Profile Updated Confirmation ──────────────────────────────────────────

    @Async
    public void sendProfileUpdatedEmail(String to, String name) {
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;
                        border:1px solid #eee;border-radius:10px;overflow:hidden;">
              <div style="background:#0F3460;padding:28px 32px;">
                <h1 style="color:#fff;margin:0;font-size:22px;">🏛 CivicConnect</h1>
              </div>
              <div style="padding:36px 32px;background:#fafafa;">
                <h2 style="color:#0F3460;margin-top:0;">👤 Profile Updated</h2>
                <p style="color:#555;line-height:1.7;">
                  Hello <b>%s</b>, your profile information was updated successfully.
                </p>
                <p style="color:#555;">
                  If you did not make this change, please contact support immediately.
                </p>
              </div>
            </div>
            """.formatted(name);

        send(to, "Your CivicConnect profile was updated", html);
    }


    private void send(String to, String subject, String html) {
        try {
            MimeMessage msg = mailer.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(from);
            h.setTo(to);
            h.setSubject(subject);
            h.setText(html, true);
            mailer.send(msg);
            log.info(" Email sent → {} | {}", to, subject);
        } catch (MessagingException e) {
            log.error(" Email failed → {} | {}", to, e.getMessage());
        }
    }
}
