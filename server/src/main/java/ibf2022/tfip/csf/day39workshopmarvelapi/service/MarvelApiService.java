package ibf2022.tfip.csf.day39workshopmarvelapi.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.tfip.csf.day39workshopmarvelapi.model.MarvelCharacter;

@Service
public class MarvelApiService {
    @Value("${MARVEL_API_URL}")
    private String marvelApiUrl;

    @Value("${MARVEL_API_PRIVATE_KEY}")
    private String marvelApiPrivKey;

    @Value("${MARVEL_API_PUBLIC_KEY}")
    private String marvelApiPubKey;

    private String[] getMarvelApiHash() {
        String[] result = new String[2];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long tsVal = timestamp.getTime();
        String hashVal = tsVal + marvelApiPrivKey + marvelApiPubKey;
        result[0] = tsVal + "";
        result[1] = DigestUtils.md5Hex(hashVal);
        return result;
    }

    public Optional<List<MarvelCharacter>> getCharacters(String characterName, Integer limit, Integer offset)
            throws IOException {
        System.out.println(characterName);

        ResponseEntity<String> resp = null;
        List<MarvelCharacter> c = null;
        String[] r = getMarvelApiHash();

        String marvelApiCharsUrl = UriComponentsBuilder
                .fromUriString(marvelApiUrl + "characters")
                .queryParam("ts", r[0])
                .queryParam("apikey", marvelApiPubKey.trim())
                .queryParam("hash", r[1])
                .queryParam("nameStartsWith", characterName.replaceAll(" ", "%20"))
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .toUriString();

        RestTemplate rTemplate = new RestTemplate();
        resp = rTemplate.getForEntity(marvelApiCharsUrl, String.class);

        try {
            c = MarvelCharacter.create((resp.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (c != null)
            return Optional.of(c);
        return Optional.empty();
    }

    public Optional<MarvelCharacter> getCharacterDetails(String charId) {
        ResponseEntity<String> resp = null;
        List<MarvelCharacter> cArr = null;
        MarvelCharacter c = null;

        
        String[] r = getMarvelApiHash();
        String marvelApiCharsUrl = UriComponentsBuilder
                .fromUriString(marvelApiUrl + "characters/" + charId)
                .queryParam("ts", r[0])
                .queryParam("apikey", marvelApiPubKey.trim())
                .queryParam("hash", r[1])
                .toUriString();

        RestTemplate rTemplate = new RestTemplate();
        resp = rTemplate.getForEntity(marvelApiCharsUrl, String.class);
        
        try {
            cArr = MarvelCharacter.create((resp.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        c = cArr.get(0);
        if (cArr != null)
            return Optional.of(c);
        return Optional.empty();
    }

}
