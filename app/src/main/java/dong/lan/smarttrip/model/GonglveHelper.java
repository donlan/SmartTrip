package dong.lan.smarttrip.model;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午7:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GonglveHelper {
    private static final String BASE_URL = "http://www.mafengwo.cn/gonglve/";
    private static final String TAG = "GonglveHelper";

    private static GonglveHelper instance;
    private String ids;
    private int page = 0;

    private GonglveHelper() {
    }

    public static GonglveHelper getInstance() {
        if (instance == null) {
            instance = new GonglveHelper();
        }
        return instance;
    }

    /**
     * 首次从网页加载数据
     *
     * @param subscriber
     */
//    public void loadDate(Subscriber<List<Gonglve>> subscriber) {
//        Single<List<Gonglve>> listSingle = Single.fromCallable(new Callable<List<Gonglve>>() {
//            @Override
//            public List<Gonglve> call() throws Exception {
//                try {
//                    Connection.Response response = Jsoup.connect(BASE_URL).timeout(5000).execute();
//                    Document document = Jsoup.parse(response.body());
//                    parseIds(document);
//                    return prase(document, false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        });
//        listSingle.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//
//    }
//
//    /**
//     * 加载更多内容（模拟在网页上的点击加载更多）
//     *
//     * @param subscriber
//     */
//    public void loadMore(Subscriber<List<Gonglve>> subscriber) {
//        Single.fromCallable(new Callable<List<Gonglve>>() {
//            @Override
//            public List<Gonglve> call() throws Exception {
//                try {
//                    Connection.Response response = createData()
//                            .timeout(5000)
//                            .execute();
//
//                    Document document = Jsoup.parse(response.body());
//                    Log.d(TAG, "call: " + document.body());
//                    parseIds(document);
//                    return prase(document, true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//
//    /**
//     * 解析html页面中的攻略条目
//     *
//     * @param document
//     * @return
//     */
//    private List<Gonglve> prase(Document document, boolean isMorePart) {
//        if (document == null)
//            return null;
//        Elements elements;
//        if (isMorePart) {
//            elements = document.getElementsByClass("_j_feed_data");
//        } else {
//            elements = document.getElementsByClass("gl-post");
//        }
//        if (elements == null || elements.isEmpty())
//            return null;
//        List<Gonglve> gonglves = new ArrayList<>();
//        Elements elements1 = elements.first().getElementsByClass("feed-item _j_feed_item");
//        for (Element element : elements1) {
//            gonglves.add(new Gonglve.Builder()
//                    .tittle(element.getElementsByClass("title").text())
//                    .text(element.getElementsByClass("info").text())
//                    .url(element.getElementsByTag("a").first().attr("href"))
//                    .start(parseStart(element.getElementsByClass("num").first()))
//                    .addImageUrls(praseImageUrls(element.getElementsByTag("img")))
//                    .build()
//            );
//            //Log.d(TAG, gonglves.get(gonglves.size() - 1).toString());
//        }
//        return gonglves;
//    }
//
//    /**
//     * 解析当前请求页面的ids
//     *
//     * @param doc
//     */
//    private void parseIds(Document doc) {
//        Elements e = doc.getElementsByClass("cont-main");
//        if (e == null || e.first() == null)
//            return;
//        String idsStr = e.first().attr("data-ids");
//        if (idsStr == null || idsStr.equals(""))
//            return;
//        idsStr = idsStr.substring(1, idsStr.length() - 1);
//        ids = idsStr;
//        page++;
//    }
//
//    /**
//     * 根据page ids生成加载更多内容的请求链接
//     *
//     * @return
//     */
//    private Connection createData() {
//        Connection con = Jsoup.connect(BASE_URL)
//                .method(Connection.Method.POST);
//        Connection.Request req = con.request();
//        req.requestBody();
//        StringBuilder sb = new StringBuilder();
//        sb.append("page=");
//        sb.append(getPage());
//        String s[] = getCurrentIds().split(",");
//        for (int i = 0; i < s.length; i++) {
//            sb.append("&ids%5B%5D=");
//            sb.append(s[i].substring(1, s[i].length() - 1));
//        }
//        req.requestBody(sb.toString());
//        con.request(req);
//        Log.d(TAG, "createData:" + con.request().requestBody());
//        return con;
//    }
//
//    /**
//     * @return 当前加载的页
//     */
//    private String getPage() {
//        return String.valueOf(page++);
//    }
//
//    /**
//     * @return 当前页对应的 ids
//     */
//    private String getCurrentIds() {
//        return ids == null ? "" : ids;
//    }
//
//    /**
//     * 解析当前item的点赞数
//     *
//     * @param e
//     * @return
//     */
//    private int parseStart(Element e) {
//        if (e == null || e.text() == null || e.text().equals(""))
//            return 0;
//        else
//            return Integer.parseInt(e.text());
//    }
//
//    /**
//     * 解析当前html标签下的图片链接
//     *
//     * @param elements
//     * @return
//     */
//    private List<String> praseImageUrls(Elements elements) {
//        if (elements == null)
//            return null;
//        List<String> urls = new ArrayList<>();
//        for (Element element : elements) {
//            urls.add(element.attr("src"));
//        }
//        return urls;
//    }
}
