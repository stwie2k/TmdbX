package nyamori.moe.tmdbx.obj;

import java.util.List;

public class TVObj {

    private int page;
    private int total_results;
    private int total_pages;
    private List<TVResult> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<TVResult> getResults() {
        return results;
    }

    public void setResults(List<TVResult> results) {
        this.results = results;
    }

    public static class TVResult
    {
        private String original_name;
        private long id;
        private String name;
        private int vote_count;
        private double vote_average;
        private String poster_path;
        private String first_air_date;
        private double popularity;
        private List<Integer> genre_ids ;
        private String original_language ;
        private String backdrop_path ;
        private String overview ;

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPopularity() {
            return popularity;
        }

        public double getVote_average() {
            return vote_average;
        }

        public int getVote_count() {
            return vote_count;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public String getFirst_air_date() {
            return first_air_date;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public String getOriginal_name() {
            return original_name;
        }

        public String getOverview() {
            return overview;
        }

        public void setFirst_air_date(String first_air_date) {
            this.first_air_date = first_air_date;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public void setOriginal_name(String original_name) {
            this.original_name = original_name;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

    }

}


