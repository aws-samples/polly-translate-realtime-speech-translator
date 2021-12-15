/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.voicetranslator.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;

public class LambdaHandler implements RequestHandler<Input, String> {

	AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
	AmazonTranslate translate = AmazonTranslateClient.builder().build();
	AmazonPolly polly = AmazonPollyClientBuilder.defaultClient();

	/**
	 * Lambda request handler function
	 */
	@Override
	public String handleRequest(Input input, Context context) {

		LambdaLogger logger = context.getLogger();

		logger.log(input.toString());

		String transcript = input.getData();

		// Translating text from one language to another using Amazon Translate service.
		String translatedText = translate(logger, transcript, input.getSourceLanguage(), input.getTargetLanguage());

		// Converting text to Audio using Amazon Polly service.
		String outputFile = synthesize(logger, translatedText, input.getTargetLanguage());

		// Saving output file on S3.
		String fileName = saveOnS3(logger, input.getBucket(), outputFile);

		return fileName;
	}

	/**
	 * Save the output audio file to S3
	 * @param logger
	 * @param bucket
	 * @param outputFile
	 * @return
	 */
	private String saveOnS3(LambdaLogger logger, String bucket, String outputFile) {

		logger.log("Bucket:" + bucket);

		String fileName = "output/" + new Date().getTime() + ".mp3";

		logger.log("File Name:" + fileName);

		logger.log("Output file: " + outputFile);

		PutObjectRequest request = new PutObjectRequest(bucket, fileName, new File(outputFile));

		s3.putObject(request);

		return fileName;

	}

	/**
	 * Translate the text from source to target language
	 * @param logger
	 * @param text
	 * @param sourceLanguage
	 * @param targetLanguage
	 * @return
	 */
	private String translate(LambdaLogger logger, String text, String sourceLanguage, String targetLanguage) {

		if (targetLanguage.equals("ca")) {
			targetLanguage = "fr";
		}

		if (targetLanguage.equals("gb")) {
			targetLanguage = "en";
		}

		TranslateTextRequest request = new TranslateTextRequest().withText(text).withSourceLanguageCode(sourceLanguage)
				.withTargetLanguageCode(targetLanguage);
		TranslateTextResult result = translate.translateText(request);

		String translatedText = result.getTranslatedText();

		logger.log("Translation: " + translatedText);

		return translatedText;

	}

	/**
	 * Get translated Polly output file in target language
	 * @param logger
	 * @param text
	 * @param language
	 * @return
	 */
	private String synthesize(LambdaLogger logger, String text, String language) {

		VoiceId voiceId = null;

		if (language.equals("en")) {
			voiceId = VoiceId.Matthew;
		}

		if (language.equals("pl")) {
			voiceId = VoiceId.Maja;
		}

		if (language.equals("es")) {
			voiceId = VoiceId.Miguel;
		}

		if (language.equals("fr")) {
			voiceId = VoiceId.Mathieu;
		}

		if (language.equals("ja")) {
			voiceId = VoiceId.Takumi;
		}

		if (language.equals("ru")) {
			voiceId = VoiceId.Maxim;
		}

		if (language.equals("de")) {
			voiceId = VoiceId.Hans;
		}

		if (language.equals("it")) {
			voiceId = VoiceId.Giorgio;
		}

		if (language.equals("sv")) {
			voiceId = VoiceId.Astrid;
		}

		if (language.equals("gb")) {
			voiceId = VoiceId.Brian;
		}

		if (language.equals("ca")) {
			voiceId = VoiceId.Chantal;
		}
		if (language.equals("hi")) {
			voiceId = VoiceId.Aditi;
		}

		String outputFileName = "/tmp/output.mp3";

		SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
				.withOutputFormat(OutputFormat.Mp3).withVoiceId(voiceId).withText(text);

		try (FileOutputStream outputStream = new FileOutputStream(new File(outputFileName))) {
			SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
			byte[] buffer = new byte[2 * 1024];
			int readBytes;

			try (InputStream in = synthesizeSpeechResult.getAudioStream()) {
				while ((readBytes = in.read(buffer)) > 0) {
					outputStream.write(buffer, 0, readBytes);
				}
			}

		} catch (Exception e) {
			logger.log(e.toString());
		}

		return outputFileName;
	}
}

/**
 * Request object
 *
 */
class Input {
	String data;
	String sourceLanguage;
	String targetLanguage;
	String bucket;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public String getTargetLanguage() {
		return targetLanguage;
	}

	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	@Override
	public String toString() {
		return "Input [data=" + data + ", sourceLanguage=" + sourceLanguage + ", targetLanguage=" + targetLanguage
				+ ", bucket=" + bucket + "]";
	}

}
