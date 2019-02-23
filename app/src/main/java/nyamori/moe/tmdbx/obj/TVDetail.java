package nyamori.moe.tmdbx.obj;

import java.util.List;

public class TVDetail {

    private String backdrop_path;
    private List<CreatedBy> created_by;
    private List<Integer> episode_run_time;
    private String first_air_date;
    private List<Genre> genres;
    private String homepage;
    private int id;
    private boolean in_production;
    private List<String> languages;
    private String last_air_date;
    private String name;
    private List<Network> networks;
    private int number_of_episodes;
    private int number_of_seasons;
    private List<String> origin_country;
    private String original_language;
    private String original_name;
    private String overview;
    private double popularity;
    private String poster_path;
    private List<ProductionCompany> production_companies;
    private List<Season> seasons;
    private String status;
    private String type;
    private double vote_average;
    private int vote_count;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public boolean isIn_production() {
        return in_production;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public List<CreatedBy> getCreated_by() {
        return created_by;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Integer> getEpisode_run_time() {
        return episode_run_time;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public void setCreated_by(List<CreatedBy> created_by) {
        this.created_by = created_by;
    }

    public void setEpisode_run_time(List<Integer> episode_run_time) {
        this.episode_run_time = episode_run_time;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setIn_production(boolean in_production) {
        this.in_production = in_production;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setLast_air_date(String last_air_date) {
        this.last_air_date = last_air_date;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public void setNumberOfEpisodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public String getOverview() {
        return overview;
    }

    public void setNumberOfSeasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public double getVote_average() {
        return vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductionCompany> getProduction_companies() {
        return production_companies;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public void setProduction_companies(List<ProductionCompany> production_companies) {
        this.production_companies = production_companies;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Genre {
        private long id;
        private String name;

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }
    }

    public static class CreatedBy
    {
        private int id;
        private String name;
        private int gender;
        private String profile_path;

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

        public int getGender() {
            return gender;
        }

        public String getProfile_path() {
            return profile_path;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setProfile_path(String profile_path) {
            this.profile_path = profile_path;
        }
    }

    public static class Network
    {
        private String name;
        private int id;
        private String logo_path;
        private String origin_country;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOrigin_country(String origin_country) {
            this.origin_country = origin_country;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public String getOrigin_country() {
            return origin_country;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }
    }

    public static class Season
    {
        private String air_date;
        private int episode_count;
        private int id;
        private String name;
        private String overview;
        private String poster_path;
        private int season_number;

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getOverview() {
            return overview;
        }

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

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public int getEpisode_count() {
            return episode_count;
        }

        public int getSeason_number() {
            return season_number;
        }

        public String getAir_date() {
            return air_date;
        }

        public void setAir_date(String air_date) {
            this.air_date = air_date;
        }

        public void setEpisode_count(int episode_count) {
            this.episode_count = episode_count;
        }

        public void setSeason_number(int season_number) {
            this.season_number = season_number;
        }
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

}


