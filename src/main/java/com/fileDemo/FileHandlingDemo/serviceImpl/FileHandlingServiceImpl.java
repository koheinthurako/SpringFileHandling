package com.fileDemo.FileHandlingDemo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fileDemo.FileHandlingDemo.Service.FileHandlingService;

public class FileHandlingServiceImpl implements FileHandlingService{
	
	private final Path storePath;
	
//	Controller ဘက်ကနေ upload ကိုခိုင်းလိုက်ပြီဆိုတာနဲ့ ဒီထဲက constructor က အရင်အလုပ်လုပ်သွားတယ် ပြီးမှအောက်က upload method လုပ်တယ်
	
	@Autowired
	public FileHandlingServiceImpl() throws IOException {
//		storePath က ဒီ spring project ရဲ့ src/main/resources/static ထဲမှာ File ကို variable အနေနဲ့ သတ်မှတ်လိုက်တယ်
//		resolve ဆိုတာ "/" တွေနဲ့ဆင်တူတယ် 
		Path storePath = Paths.get("").resolve("src").resolve("main")
				.resolve("resources").resolve("static").resolve("File");
//		ပြီးမှ အဲ့ဒီလမ်းကြောင်းမှာ "File" ဆိုတဲ့နာမည်နဲ့ file တကယ်ရှိမရှိစစ်တယ် မရှိရင် create လိုက်တယ်
		if(Files.exists(storePath)) {
			Files.createDirectories(storePath);
		}
		this.storePath = storePath;
	}

	@Override
	public String upload(MultipartFile file, String type) throws IOException {
//		ဒီ method အလုပ်လုပ်တော့ controller body ကနေ ဘယ် file, ဘာအမျိုးအစားဆိုတဲ့ data တွေပါလာတယ် 
		String fileName = LocalDate.now().getYear()+"_"+StringUtils.cleanPath(file.getOriginalFilename());
//		ပြီးမှ fileName ပေးဖို့ အပေါ်က LocalDate.now().getYear() ကိုသုံးပြီးတော့ StringUtils.cleanPath API ကိုသုံးသွားတယ်
//		StringUtils.cleanPath က D://test/abc.txt ဆိုပြီးရှိတယ်ဆိုရင် txt ဆိုတဲ့ file name ကိုပဲဖြတ်ယူပေးနိုင်တဲ့ API
//		အဲ့တာကိုသုံးပြီးတော့မှ "2024_abc" ဆိုတဲ့ပုံစံမျိုး fileName ကိုပေးလိုက်တယ်
		Files.copy(file.getInputStream(), this.storePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
//		getInputStream() ဆိုတာက ဓာတ်ပုံတွေ၊ သီချင်းတွေ၊ စာတွေ သယ်ချင်တယ်ဆိုရင် သုံးပေးရတယ် အဲ့တာကို copy ကူးပြီးတော့ ဘယ်ထဲထည့်မလဲဆိုရင်
//		constructor ထဲက ဖန်တီးထားတဲ့ "SRC/main/resources/static/file လမ်းကြောင်းထဲမှာရှိတဲ့ အပေါ်က fileName ဆိုတဲ့အထဲကိုကူးထည့်မယ်
//		ရှိပြီးသားဆိုရင် StandardCopyOption နဲ့ replace လုပ်မယ်ဆိုတဲ့သဘောဖြစ်တယ်
		return fileName;
//		ပြီးရင်တော့ fileName ဆိုတဲ့ String တစ်ခု return ပြန်ပေးလိုက်တယ်
	}

	@Override
	public byte[] getFile(String fileName) throws IOException {
//		controller ကနေ ဒီ method အလုပ်လုပ်တော့ ဟိုးပေါ်ဆုံးက constructor စအလုပ်လုပ်တာနဲ့ file ဆောက်ပေးသွားတဲ့ storePath ထဲကတစ်ဆင့်
		byte[] fileByte = null;
//		အောက်က path မှာ storePath/fileName ဆိုပြီးဆင်းရှာ သတ်မှတ်လိုက်တယ်
		Path path = this.storePath.resolve(fileName);
		Resource resource = new UrlResource(path.toUri());
		if(resource.exists() && resource.isReadable()) {
			fileByte = StreamUtils.copyToByteArray(resource.getInputStream());
		}
		return fileByte;
	}
	
	

}
