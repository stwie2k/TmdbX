package nyamori.moe.tmdbx.obj;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;


import java.util.List;

public class MovieDetail {
    private boolean adult;
    private String backdrop_path;
    private BelongsToCollection belongs_to_collection;
    private long budget;
    private List<Genre> genres;
    private String homepage;
    private long id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private List<ProductionCompany> production_companies;
    private List<ProductionCountry> production_countries;
    private String release_date;
    private long revenue;
    private long runtime;
    private List<SpokenLanguage> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double vote_average;
    private long vote_count;
    private Casts casts;

    public boolean getAdult() { return adult; }
    public void setAdult(boolean value) { this.adult = value; }

    public String getBackdropPath() { return backdrop_path; }
    public void setBackdropPath(String value) { this.backdrop_path = value; }

    public Object getBelongsToCollection() { return belongs_to_collection; }
    public void setBelongsToCollection(BelongsToCollection value) { this.belongs_to_collection = value; }

    public long getBudget() { return budget; }
    public void setBudget(long value) { this.budget = value; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> value) { this.genres = value; }

    public String getHomepage() { return homepage; }
    public void setHomepage(String value) { this.homepage = value; }

    public long getId() { return id; }
    public void setId(long value) { this.id = value; }

    public String getImdbId() { return imdb_id; }
    public void setImdbId(String value) { this.imdb_id = value; }

    public String getOriginalLanguage() { return original_language; }
    public void setOriginalLanguage(String value) { this.original_language = value; }

    public String getOriginalTitle() { return original_title; }
    public void setOriginalTitle(String value) { this.original_title = value; }

    public String getOverview() { return overview; }
    public void setOverview(String value) { this.overview = value; }

    public double getPopularity() { return popularity; }
    public void setPopularity(double value) { this.popularity = value; }

    public String getPosterPath() { return poster_path; }
    public void setPosterPath(String value) { this.poster_path = value; }

    public List<ProductionCompany> getProductionCompanies() { return production_companies; }
    public void setProductionCompanies(List<ProductionCompany> value) { this.production_companies = value; }

    public List<ProductionCountry> getProductionCountries() { return production_countries; }
    public void setProductionCountries(List<ProductionCountry> value) { this.production_countries = value; }

    public String getReleaseDate() { return release_date; }
    public void setReleaseDate(String value) { this.release_date = value; }

    public long getRevenue() { return revenue; }
    public void setRevenue(long value) { this.revenue = value; }

    public long getRuntime() { return runtime; }
    public void setRuntime(long value) { this.runtime = value; }

    public List<SpokenLanguage> getSpokenLanguages() { return spoken_languages; }
    public void setSpokenLanguages(List<SpokenLanguage> value) { this.spoken_languages = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getTagline() { return tagline; }
    public void setTagline(String value) { this.tagline = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public boolean getVideo() { return video; }
    public void setVideo(boolean value) { this.video = value; }

    public double getVoteAverage() { return vote_average; }
    public void setVoteAverage(double value) { this.vote_average = value; }

    public long getVoteCount() { return vote_count; }
    public void setVoteCount(long value) { this.vote_count = value; }

    public Casts getCasts() { return casts; }
    public void setCasts(Casts value) { this.casts = value; }

    public static class Casts {
        private List<Cast> cast;
        private List<Crew> crew;

        public List<Cast> getCast() { return cast; }
        public void setCast(List<Cast> value) { this.cast = value; }

        public List<Crew> getCrew() { return crew; }
        public void setCrew(List<Crew> value) { this.crew = value; }
    }

    public static class BelongsToCollection
    {
        private int id ;
        private String name ;
        private String poster_path ;
        private String backdrop_path ;

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }
    }

    public static class Cast {
        private long cast_id;
        private String character;
        private String credit_id;
        private long gender;
        private long id;
        private String name;
        private long order;
        private String profile_path;

        public long getCastId() { return cast_id; }
        public void setCastId(long value) { this.cast_id = value; }

        public String getCharacter() { return character; }
        public void setCharacter(String value) { this.character = value; }

        public String getCreditId() { return credit_id; }
        public void setCreditId(String value) { this.credit_id = value; }

        public long getGender() { return gender; }
        public void setGender(long value) { this.gender = value; }

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

        public long getOrder() { return order; }
        public void setOrder(long value) { this.order = value; }

        public String getProfilePath() { return profile_path; }
        public void setProfilePath(String value) { this.profile_path = value; }
    }

    public static class Crew {
        private String credit_id;
        private String department;
        private long gender;
        private long id;
        private String job;
        private String name;
        private String profile_path;

        public String getCreditId() { return credit_id; }
        public void setCreditId(String value) { this.credit_id = value; }

        public String getDepartment() { return department; }
        public void setDepartment(String value) { this.department = value; }

        public long getGender() { return gender; }
        public void setGender(long value) { this.gender = value; }

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public String getJob() { return job; }
        public void setJob(String value) { this.job = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

        public String getProfilePath() { return profile_path; }
        public void setProfilePath(String value) { this.profile_path = value; }
    }

    public static class Genre {
        private long id;
        private String name;

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }
    }

    public static class ProductionCompany {
        private long id;
        private String logo_path;
        private String name;
        private String origin_country;

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public String getLogoPath() { return logo_path; }
        public void setLogoPath(String value) { this.logo_path = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

        public String getOriginCountry() { return origin_country; }
        public void setOriginCountry(String value) { this.origin_country = value; }
    }

    public static class ProductionCountry {
        private String iso3166_1;
        private String name;

        public String getIso31661() { return iso3166_1; }
        public void setIso31661(String value) { this.iso3166_1 = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }
    }

    public static class SpokenLanguage {
        private String iso639_1;
        private String name;

        public String getIso6391() { return iso639_1; }
        public void setIso6391(String value) { this.iso639_1 = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

    }
}
