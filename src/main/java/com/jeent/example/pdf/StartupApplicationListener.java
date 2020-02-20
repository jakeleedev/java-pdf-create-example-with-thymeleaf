package com.jeent.example.pdf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartupApplicationListener {

	@Autowired
	PdfGeneratorUtil pdfGeneratorUtil;
	
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	
    	User[] user = {User.builder().id(1).name("user1").build(), User.builder().id(2).name("user2").build()};
    	parameters.put("resultList", user);
    	
    	String path = pdfGeneratorUtil.createPdf("pdf", parameters, Locale.KOREA);
    	
    	log.info(path);
    
    }
}
