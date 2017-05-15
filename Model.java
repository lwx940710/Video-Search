
import com.google.api.services.youtube.model.SearchResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.List;

public class Model extends Observable {
    /**
     * The observers that are watching this model for changes.
     */
    private ArrayList<Observer> observers;

    private JFrame frame;

    private int top_width, top_height;
    private boolean is_grid;
    private int star;
    private String keyword;
    private int max_cols;
    private boolean loading;
    private CenterView cv;
    //private ArrayList<SearchResult> search_results;
    public class Search_result {
        private String video_id;
        private String title;
        private String relative_date;
        private BufferedImage img;
        private int rating;
        private URL url;

        Search_result(String id, String t, String date, BufferedImage bi) {
            video_id = id;
            title = t;
            relative_date = date;
            img = bi;
            rating = 0;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String s) {
            video_id = s;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String s) {
            title = s;
        }

        public String getRelative_date() {
            return relative_date;
        }

        public void setRelative_date(String s) {
            relative_date = s;
        }

        public BufferedImage getImg() {
            return img;
        }

        public void setImg(BufferedImage bi) {
            img = bi;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int r) {
            rating = r;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL u) {
            url = u;
        }
    }
    private int num_results;
    private ModelYouTube youtube;
    private ArrayList<Search_result> search_results;

    public Model(JFrame frame, ModelYouTube youtube) {
        this.observers = new ArrayList();
        this.frame = frame;
        this.youtube = youtube;
        is_grid = true;
        star = 0;
        search_results = new ArrayList<Search_result>();
        keyword = "";
        max_cols = 3;
        loading = false;

        setChanged();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void clearAll() {
        keyword = "";
        star = 0;
        search_results = new ArrayList<Search_result>();
        num_results = 0;
    }

    public Boolean save() {
        JFileChooser chooser = new JFileChooser();
        File file = null;
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            String file_name = file.getName();
            if (!file_name.endsWith(".videolst")) {
                file = new File(file.toString() + ".videolst");
            }
            chooser.setCurrentDirectory(file.getParentFile());

            if (file != null) {
                file_name = file.getName();
                try {
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);

                    // is_grid
                    if (is_grid) {
                        bw.write(Integer.toString(1));
                    } else {
                        bw.write(Integer.toString(0));
                    }
                    bw.newLine();

                    // star
                    bw.write(Integer.toString(star));
                    bw.newLine();

                    // keyword
                    bw.write(keyword);
                    bw.newLine();

                    // max_cols
                    bw.write(Integer.toString(max_cols));
                    bw.newLine();

                    // filter
                    int n = 0;
                    for (int i = 0; i < num_results; i++) {
                        Search_result sr = search_results.get(i);
                        if (sr.getRating() >= star) {
                            n += 1;
                        }
                    }

                    // num_results
                    bw.write(Integer.toString(n));
                    bw.newLine();

                    // search_results;
                    for (int i = 0; i < num_results; i++) {
                        Search_result sr = search_results.get(i);
                        if (sr.getRating() < star) {
                            continue;
                        }
                        bw.write(sr.getVideo_id());
                        bw.newLine();
                        bw.write(sr.getTitle());
                        bw.newLine();
                        bw.write(sr.getRelative_date());
                        bw.newLine();
                        bw.write(sr.getUrl().getPath());
                        bw.newLine();
                        bw.write(Integer.toString(sr.getRating()));
                        bw.newLine();

                        bw.newLine();
                    }

                    bw.close();
                    return true;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return false;
    }

    public Boolean load() {
        JFileChooser chooser = new JFileChooser();
        File file = null;
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            chooser.setCurrentDirectory(file.getParentFile());
            String file_name = file.getName();
            if (file_name.endsWith(".videolst")) {
                try {
                    Scanner scanner = new Scanner(file);

                    // read stuff
                    int ig = scanner.nextInt();
                    if (ig == 1) {
                        is_grid = true;
                    } else {
                        is_grid = false;
                    }
                    star = scanner.nextInt();
                    keyword = scanner.next();
                    max_cols = scanner.nextInt();
                    num_results = scanner.nextInt();
                    scanner.nextLine();
                    for (int i = 0; i < num_results; i++) {
                        String id = scanner.nextLine();
                        //System.out.println(id);
                        String title = scanner.nextLine();
                        //System.out.println(title);
                        String date = scanner.nextLine();
                        //System.out.println(date);
                        String url = scanner.nextLine();
                        //System.out.println(url);
                        int r = scanner.nextInt();
                        // String id, String t, String date, BufferedImage bi
                        BufferedImage img = null;
                        try {
                            URL pic_url = new URL("https://i.ytimg.com" + url);
                            img = ImageIO.read(pic_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Search_result sr = new Search_result(id, title, date, img);
                        sr.setRating(r);
                        search_results.add(sr);
                        scanner.nextLine();
                        scanner.nextLine();
                    }
                    update();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public int getTop_width() {
        return top_width;
    }

    public void setTop_width(int i) {
        top_width = i;
    }

    public int getTop_height() {
        return top_height;
    }

    public void setTop_height(int i) {
        top_height = i;
    }

    public boolean getIs_grid() {
        return is_grid;
    }

    public void setIs_grid(boolean b) {
        is_grid = b;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int i) {
        star = i;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String str) {
        keyword = str;
    }

    public CenterView getCV() {
        return cv;
    }

    public void setCv(CenterView c) {
        cv = c;
    }

    public Search_result getSearch_results(int i) {
        return search_results.get(i);
    }

    public void setSearch_results(int i, Search_result sr) {
        search_results.set(i, sr);
    }

    public int getNum_results() {
        return num_results;
    }

    public void setNum_results(int i) {
        num_results = i;
    }

    public int getMax_cols() {
        return max_cols;
    }

    public void setMax_cols(int i) {
        max_cols = i;
    }

    public boolean getLoading() {
        return loading;
    }

    public void setLoading(boolean b) {
        loading = b;
    }

    public void paintStars(JButton s1, JButton s2, JButton s3, JButton s4, JButton s5, int r) {
        if (r >= 1) {
            s1.setIcon(new ImageIcon("Star_selected.png"));
        } else {
            s1.setIcon(new ImageIcon("Star_unselected.png"));
        }
        if (r >= 2) {
            s2.setIcon(new ImageIcon("Star_selected.png"));
        } else {
            s2.setIcon(new ImageIcon("Star_unselected.png"));
        }
        if (r >= 3) {
            s3.setIcon(new ImageIcon("Star_selected.png"));
        } else {
            s3.setIcon(new ImageIcon("Star_unselected.png"));
        }
        if (r >= 4) {
            s4.setIcon(new ImageIcon("Star_selected.png"));
        } else {
            s4.setIcon(new ImageIcon("Star_unselected.png"));
        }
        if (r == 5) {
            s5.setIcon(new ImageIcon("Star_selected.png"));
        } else {
            s5.setIcon(new ImageIcon("Star_unselected.png"));
        }
    }

    private String getTimeDiff(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        String hour = date.substring(11, 13);
        String minute = date.substring(14, 16);
        String second = date.substring(17, 19);
        date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        String now = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime_from = LocalDateTime.parse(date, formatter);
        LocalDateTime dateTime_to = LocalDateTime.parse(now, formatter);
        long diffInDays = java.time.Duration.between(dateTime_from, dateTime_to).toDays();
        if (diffInDays >= 730) {
            long n = diffInDays / 365;
            return (n + " years ago");
        } else if (diffInDays >= 365) {
            return ("1 year ago");
        } else if (diffInDays >= 60) {
            long n = diffInDays / 30;
            return (n + " months ago");
        } else if (diffInDays >= 30) {
            return ("1 month ago");
        } else if (diffInDays >= 2) {
            return (diffInDays + " days ago");
        } else if (diffInDays >= 1) {
            return ("1 day ago");
        } else {
            long diffInHours = java.time.Duration.between(dateTime_from, dateTime_to).toHours();
            if (diffInHours >= 2) {
                return (diffInHours + " hours ago");
            } else if (diffInHours >= 1) {
                return ("1 hour ago");
            } else {
                return ("A moment ago");
            }
        }
    }

    private String trunc(String str) {
        int end = str.indexOf(".jpg") + 4;
        str = str.substring(end);
        end = str.indexOf(".jpg") + 4;
        str = str.substring(end);
        int start = str.indexOf("http");
        end = str.indexOf(".jpg") + 4;
        return str.substring(start, end);
    }

    public void searchVideos(String kw) {
        ArrayList<SearchResult> results = youtube.searchVideos(kw);
        //search_results = results;
        for (int i = 0; i < results.size(); i++) {
            SearchResult sr = results.get(i);
            String id = sr.getId().getVideoId();
            String t = sr.getSnippet().getTitle();
            String rd = getTimeDiff(sr.getSnippet().getPublishedAt().toString());
            BufferedImage bi = null;
            try {
                String link = trunc(sr.toString());
                URL pic_url = new URL(link);
                bi = ImageIO.read(pic_url);

                Search_result r = new Search_result(id, t, rd, bi);
                r.setUrl(pic_url);
                search_results.add(r);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        num_results = results.size();

        loading = false;
    }

    public void update() {
        setChanged();
        notifyObservers();
    }

}
