package com.jeent.example.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

@Component
public class PdfGeneratorUtil {

	@Autowired
	private TemplateEngine templateEngine;

	public String createPdf(String templateName, Map<String, Object> map, Locale locale) {
		Context ctx = new Context();
		Iterator<Entry<String, Object>> itMap = map.entrySet().iterator();
		// 파라미터를 Context에 추가
		while (itMap.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) itMap.next();
			ctx.setVariable(pair.getKey().toString(), pair.getValue());
			
			// Spring 메세지 사용 중 언어에 따라 메세지 내용을 변경하기 위하여 설정
			ctx.setLocale(locale);
		}

		// 템플릿 엔진을 이용하여 html 문자열 생성
		String processedHtml = templateEngine.process(templateName, ctx);

		FileOutputStream os = null;
		
		// 랜덤한 PDF파일 이름 생성
		String fileName = UUID.randomUUID().toString();
		try {

			// 임시파일 생성
			final File outputFile = File.createTempFile(fileName, ".pdf");
			os = new FileOutputStream(outputFile);

			ITextRenderer renderer = new ITextRenderer();
			
			// 한글 표시를 위하여 폰트 설정
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(new ClassPathResource("fonts/NanumGothic.ttf").getPath(), BaseFont.IDENTITY_H, true);

			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			
			// 생성된 PDF의 경로를 반환
			return outputFile.getPath();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
