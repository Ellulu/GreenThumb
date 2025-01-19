package com.helmo.greenThumb.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;
import com.helmo.greenThumb.dto.AdviceDTO;
import com.helmo.greenThumb.model.Advice;
import com.helmo.greenThumb.services.AdviceService;
import com.helmo.greenThumb.utils.FileValidator;

@RestController
@RequestMapping("/api/advices")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class AdviceController {
    
    @Autowired
    private AdviceService adviceService;
    
    @PostMapping
    public Advice createAdvice(@RequestAttribute("firebaseToken") FirebaseToken token,
                                 @RequestParam("advice") String adviceJson,
                                 @RequestParam(value = "pictures",required = false)List<MultipartFile> pictures) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Advice advice = objectMapper.readValue(adviceJson, Advice.class);
            System.out.println(pictures);
            if (!token.getUid().equals(advice.getAuthor().getUid())) return new Advice();
            if (pictures != null) {
                Bucket bucket = StorageClient.getInstance().bucket("greenthumb-54c99.firebasestorage.app");
                for (var picture : pictures) {
                    if (FileValidator.validateImage(picture)) {

                        String blobName = "advice/" + token.getUid() + "/" + new Date().getTime() + "-" + picture.getOriginalFilename();
                        Blob blob = bucket.create(blobName, picture.getInputStream());

                        String encodedBlobName = URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8.toString());
                        String pictureUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucket.getName(), encodedBlobName);
                        System.out.println(pictureUrl);
                        advice.getFiles().add(pictureUrl);
                    }
                }
            }

            return adviceService.createAdvice(advice);


        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new Advice();
    }

    @GetMapping("/page/{page}")
    public List<AdviceDTO> getAdviceByPage(@RequestAttribute("firebaseToken") FirebaseToken token, @PathVariable int page) {
        System.out.println(">>> Page: " + page);
        return adviceService.getAdviceByPage(token.getUid(), page);
    }

    @GetMapping("/{id}")
    public Advice getAdviceById(@PathVariable Long id) {
        return adviceService.getAdviceById(id);
    }


    @DeleteMapping("/{id}")
    public void deleteAdvice(
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @PathVariable Long id) {
        Advice advice = adviceService.getAdviceById(id);
        if (advice == null) return;
        if (!advice.getAuthor().getUid().equals(token.getUid())) return;
        adviceService.deleteAdvice(id);
    }
}
