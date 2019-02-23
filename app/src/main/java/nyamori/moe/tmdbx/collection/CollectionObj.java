package nyamori.moe.tmdbx.collection;

import java.util.UUID;

public class CollectionObj {

    public enum CollectionType{
        MOVIE("movie"), TV("tv");
        private final String mTypeString;
        CollectionType(String typeString){
            this.mTypeString = typeString;
        }
        public String getType(){
            return mTypeString;
        }
        public String toString(){
            return this.mTypeString;
        }
        public static CollectionType fromString(String typeString){
            for(CollectionType type : CollectionType.values()){
                if(type.getType().equals(typeString)){
                    return type;
                }
            }
            return null;
//            if(typeString.equals("movie")){
//                return Movie;
//            }
//            else return TV;
        }
    }

    private UUID mCollectingUserId;
    private String mMovieId;
    private CollectionType mType;

    public CollectionObj(String movieId, UUID collectingUserId) {
        mCollectingUserId = collectingUserId;
        mMovieId = movieId;
        this.mType = CollectionType.MOVIE;
    }

    public CollectionObj(String movieId, UUID collectingUserId, String collectionType) {
        mCollectingUserId = collectingUserId;
        mMovieId = movieId;
        mType = CollectionType.fromString(collectionType);
    }

    public UUID getCollectingUserId() {
        return mCollectingUserId;
    }

    public void setCollectingUserId(UUID collectingUserId) {
        mCollectingUserId = collectingUserId;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public void setMovieId(String movieId) {
        mMovieId = movieId;
    }

    public void setCollectionType(String collectionType){
        this.mType = CollectionType.fromString(collectionType);
    }
    public String getCollectionType(){
        return this.mType.toString();
    }
}
