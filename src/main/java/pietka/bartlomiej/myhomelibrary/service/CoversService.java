package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface CoversService {
    boolean uploadCover(MultipartFile cover, Long id);
}
