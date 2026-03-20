package org.leedtech.otp.event;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnRegistrationCompleteEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

        private final EmailService service;

        @Override
        public void onApplicationEvent(OnRegistrationCompleteEvent event) {
            this.confirmRegistration(event);
        }

        private void confirmRegistration(OnRegistrationCompleteEvent event) {
            service.sendEmailVerificationToken(event.getRequestHeaderHost(), event.getEmail());
        }
}
