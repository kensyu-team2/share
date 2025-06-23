package app.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BookSearchForm {

    // 複数の検索条件を保持するリスト
    private List<SearchCriterion> criteria = new ArrayList<>();

    // 資料区分のIDを複数保持するリスト
    private List<Integer> typeIds;
    // ジャンルIDを保持するリスト
    private List<Integer> categoryIds;
    // 表示件数（デフォルト値を10に設定）
    private Integer limit = 10;

    // ソート順
    private String sort;

    // コンストラクタ：フォーム表示時に5行分の空の検索条件を用意する
    public BookSearchForm() {
        for (int i = 0; i < 5; i++) {
            criteria.add(new SearchCriterion());
        }
    }

    /**
     * 一つの検索条件を表す、publicなstatic内部クラス
     */
    @Data
    public static class SearchCriterion {
        private String field;      // 検索対象フィールド (bookName, authorName など)
        private String query;      // 検索キーワード
        private String op = "AND"; // 結合演算子 (デフォルトはAND)
        private String match = "contains";
    }
}