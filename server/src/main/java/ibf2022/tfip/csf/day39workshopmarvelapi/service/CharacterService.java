package ibf2022.tfip.csf.day39workshopmarvelapi.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import ibf2022.tfip.csf.day39workshopmarvelapi.model.Comment;
import ibf2022.tfip.csf.day39workshopmarvelapi.model.MarvelCharacter;
import ibf2022.tfip.csf.day39workshopmarvelapi.repositories.CharCommentsRepository;

@Service
public class CharacterService {
    @Autowired
    private MarvelApiService marvelApiSvc;

    @Autowired
    private CharCommentsRepository charCommentsRepo;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Optional<List<MarvelCharacter>> getCharacters(String characterName, Integer limit, Integer offset) throws IOException{
        return this.marvelApiSvc.getCharacters(characterName, limit, offset);
    }

    public MarvelCharacter getCharacterDetails(String characterId)throws IOException{
        MarvelCharacter dd = null;
        String charDetailsJson = (String)redisTemplate.opsForValue().get(characterId);
        if(charDetailsJson != null){
            dd = MarvelCharacter.createFromCache(charDetailsJson);
        }else{
            Optional<MarvelCharacter> op = marvelApiSvc.getCharacterDetails(characterId);

            dd = op.get();
            redisTemplate.opsForValue()
                    .set(characterId, dd.toJSON().toString());

            long currentDateTime = Instant.now().getMillis();
            Date afterAdding60Mins = new Date(currentDateTime + (60 * 60 * 1000));
            redisTemplate.expireAt(characterId, afterAdding60Mins);
        }
        return dd;

    }

    public Comment insertComment(Comment c){
        return this.charCommentsRepo.insertComment(c);
    }

    public List<Comment> getAllComments(String charId){
        return this.charCommentsRepo.getAllComments(charId);
    }
}
