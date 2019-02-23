package nyamori.moe.tmdbx.obj;

import java.util.List;

public class MovieObj {
    private long page;
    private long total_results;
    private long total_pages;
    private List<Result> results;

    public long getPage() { return page; }
    public void setPage(long value) { this.page = value; }

    public long getTotalResults() { return total_results; }
    public void setTotalResults(long value) { this.total_results = value; }

    public long getTotalPages() { return total_pages; }
    public void setTotalPages(long value) { this.total_pages = value; }

    public List<Result> getResults() { return results; }
    public void setResults(List<Result> value) { this.results = value; }

    public static class Result {
        private long vote_count;
        private long id;
        private boolean video;
        private double vote_average;
        private String title;
        private double popularity;
        private String poster_path;
        private String original_language;
        private String original_title;
        private long[] genre_ids;
        private String backdrop_path;
        private boolean adult;
        private String overview;
        private String release_date;

        public long getVoteCount() { return vote_count; }
        public void setVoteCount(long value) { this.vote_count = value; }

        public long getId() { return id; }
        public void setId(long value) { this.id = value; }

        public boolean getVideo() { return video; }
        public void setVideo(boolean value) { this.video = value; }

        public double getVoteAverage() { return vote_average; }
        public void setVoteAverage(double value) { this.vote_average = value; }

        public String getTitle() { return title; }
        public void setTitle(String value) { this.title = value; }

        public double getPopularity() { return popularity; }
        public void setPopularity(double value) { this.popularity = value; }

        public String getPosterPath() { return poster_path; }
        public void setPosterPath(String value) { this.poster_path = value; }

        public String getOriginalLanguage() { return original_language; }
        public void setOriginalLanguage(String value) { this.original_language = value; }

        public String getOriginalTitle() { return original_title; }
        public void setOriginalTitle(String value) { this.original_title = value; }

        public long[] getGenreIds() { return genre_ids; }
        public void setGenreIds(long[] value) { this.genre_ids = value; }

        public String getBackdropPath() { return backdrop_path; }
        public void setBackdropPath(String value) { this.backdrop_path = value; }

        public boolean getAdult() { return adult; }
        public void setAdult(boolean value) { this.adult = value; }

        public String getOverview() { return overview; }
        public void setOverview(String value) { this.overview = value; }

        public String getReleaseDate() { return release_date; }
        public void setReleaseDate(String value) { this.release_date = value; }
    }


}
