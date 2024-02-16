package com.fileDemo.FileHandlingDemo.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fileDemo.FileHandlingDemo.Service.FileHandlingService;

@RestController
@RequestMapping("/file")
public class FileHandlingController {

//	File တွေကို server ပေါ်သို့ upload လုပ်ခြင်း စမယ်
//	Flow ကတော့ SRC/main/resources/static ထဲကို file တွေလှမ်းထည့်တာ၊ ပြန်ယူတာတွေလုပ်မှာဖြစ်တယ်

	@Autowired
	FileHandlingService service;

//	ဒီကနေ postMapping နဲ့ပေးတဲ့အခါ postman ဘက်က Body မှာ JSON အနေနဲ့ပေးမှာမဟုတ်ပဲ file အနေနဲ့ပေးမှာမို့ form-data ထဲမှာပဲစမ်းမှရမယ်
	@PostMapping("/upload/{fileType}/{file}")
	public ResponseEntity<String> upload(@PathVariable String fileType, @PathVariable("file") MultipartFile file)
			throws IOException {
//		MultipartFile ဆိုတာကတော့ ဘယ် file အမျိုးအစားမဆို လက်ခံနိုင်တဲ့ spring interface API တစ်ခု
		String result = service.upload(file, fileType);
		return ResponseEntity.ok().body(result);
//		အောက်က ရေးထားတဲ့ code က အပေါ်နဲ့အတူတူပဲ .OK() ဆိုတာ HttpStatus ကိုပဲပြောတာ အထဲမှာထည့်မယ့် body ကတော့ result ဆိုတဲ့သဘော
//		return ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping("/media/{fileType}/{fileName}")
	public ResponseEntity<?> download(
//			အပေါ်က ? က ဒီ method ကို controller ကနေ get နဲ့လှမ်းခေါ်ရင် ဘာ data type ပါလာမလဲမသိလို့ Generic သုံးထားရတာဖြစ်တယ်
			@PathVariable("fileType") String fileType,
			@PathVariable("fileName") String fileName) throws IOException {
		MediaType type = MediaType.IMAGE_PNG;
		switch (fileType) {
		case "mp4":
			type = MediaType.APPLICATION_OCTET_STREAM;
			break;
		case "jpg":
		case "jpeg":
			type = MediaType.IMAGE_JPEG;
			break;
		case "txt":
			type = MediaType.TEXT_PLAIN;
			break;
		case "png":
			type = MediaType.IMAGE_PNG;
			break;
		default:
//			method အလုပ်လုပ်ရင် controller ကနေ အပေါ်က file တွေပါတာလဲဖြစ်နိုင်သလို အောက်က default လို ဘာမှမပါတာ/တခြားဟာလည်းဖြစ်နိုင်တယ်
			return ResponseEntity.badRequest().body("Unsupported File Type");
		}
		byte[] fileBytes = service.getFile(fileName);
//		Controller ကနေပါလာတဲ့ fileName ကို serviceImple() ထဲကို parameter passing ပေးလိုက်တယ်
		if (fileBytes == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(type).body(fileBytes);
	}

}
