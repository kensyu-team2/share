---- データベースの作成
--CREATE DATABASE library_management
--  CHARACTER SET utf8mb4
--  COLLATE utf8mb4_general_ci;
--
---- アプリ用ユーザーの作成
--CREATE USER 'libuser'@'localhost'
--  IDENTIFIED BY 'StrongPassword!';
--
---- 作成したユーザーに権限付与
--GRANT ALL
--  ON library_management.*
--  TO 'libuser'@'localhost';
--
--FLUSH PRIVILEGES;


-- ===================================================================
-- テーブルの削除（外部キー制約を考慮し、依存しているテーブルから先に削除）
-- ===================================================================
DROP TABLE IF EXISTS lending;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS type;
DROP TABLE IF EXISTS category;


-- ===================================================================
-- 1. ジャンルテーブル (category)
-- ===================================================================
CREATE TABLE category (
  category_id   INT           NOT NULL AUTO_INCREMENT COMMENT 'ジャンルID',
  category_name VARCHAR(20)   NOT NULL                COMMENT 'ジャンル名',
  PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ジャンルテーブル';


-- ===================================================================
-- 2. 資料区分テーブル (type)
-- ===================================================================
CREATE TABLE type (
  type_id   INT         NOT NULL AUTO_INCREMENT COMMENT '資料区分ID',
  type_name VARCHAR(20) NOT NULL                COMMENT '資料区分名 (児童書／CD／学術書…)',
  PRIMARY KEY (type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='資料区分テーブル';


-- ===================================================================
-- 3. 配架場所テーブル (place)
-- ===================================================================
CREATE TABLE place (
  place_id   VARCHAR(20) NOT NULL COMMENT '配架場所ID (例：1F-A-1)',
  place_name VARCHAR(40) NOT NULL COMMENT '配架場所名 (例：1階 新着図書コーナー)',
  PRIMARY KEY (place_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配架場所テーブル';


-- ===================================================================
-- 4. 在庫状況テーブル (status)
-- ===================================================================
CREATE TABLE status (
  status_id   INT         NOT NULL AUTO_INCREMENT COMMENT '在庫状況ID',
  status_name VARCHAR(10) NOT NULL                COMMENT '在庫状況名 (在庫あり／貸出中…)',
  PRIMARY KEY (status_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在庫状況テーブル';


-- ===================================================================
-- 5. 会員テーブル (members)
-- ===================================================================
CREATE TABLE members (
  member_id         INT           NOT NULL AUTO_INCREMENT COMMENT '会員ID',
  name              VARCHAR(20)   NOT NULL                COMMENT '氏名',
  name_ruby         VARCHAR(40)   NOT NULL                COMMENT '氏名フリガナ',
  address           VARCHAR(40)   NOT NULL                COMMENT '住所',
  phone_number      VARCHAR(20)   NOT NULL                COMMENT '電話番号',
  mail              VARCHAR(40)   NOT NULL                COMMENT '電子メール',
  birthday          DATE          NOT NULL                COMMENT '生年月日',
  registration_date DATE          NOT NULL                COMMENT '会員登録日',
  retire_date       DATE                              NULL COMMENT '退会日',
  PRIMARY KEY (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会員テーブル';


-- ===================================================================
-- 6. 書誌情報テーブル (books)
-- ===================================================================
CREATE TABLE books (
  book_id      INT           NOT NULL AUTO_INCREMENT COMMENT '書誌情報ID',
  ISBN         VARCHAR(20)   NOT NULL                COMMENT 'ISBN',
  book_name    VARCHAR(40)   NOT NULL                COMMENT 'タイトル',
  book_ruby    VARCHAR(40)   NOT NULL                COMMENT 'タイトルフリガナ',
  author_name  VARCHAR(20)   NOT NULL                COMMENT '著者名',
  author_ruby  VARCHAR(20)   NOT NULL                COMMENT '著者名フリガナ',
  publisher    VARCHAR(40)   NOT NULL                COMMENT '出版社',
  publish_date DATE          NOT NULL                COMMENT '出版年月日',
  keyword      VARCHAR(100)                       NULL COMMENT 'キーワード',
  PRIMARY KEY (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='書誌情報テーブル';


-- ===================================================================
-- 7. 個別資料テーブル (items)
-- ===================================================================
CREATE TABLE items (
  item_id     INT           NOT NULL AUTO_INCREMENT COMMENT '個別資料ID',
  book_id     INT           NOT NULL                COMMENT '書誌情報ID',
  get_date    DATE          NOT NULL                COMMENT '入荷年月日',
  place_id    VARCHAR(20)   NOT NULL                COMMENT '配架場所ID',
  category_id INT           NOT NULL                COMMENT 'ジャンルID',
  type_id     INT           NOT NULL                COMMENT '資料区分ID',
  delete_date DATE                              NULL COMMENT '廃棄年月日',
  lost_date   DATE                              NULL COMMENT '紛失年月日',
  status_id   INT           NOT NULL                COMMENT '在庫状況ID',
  PRIMARY KEY (item_id),
  FOREIGN KEY (book_id)     REFERENCES books   (book_id),
  FOREIGN KEY (place_id)    REFERENCES place   (place_id),
  FOREIGN KEY (category_id) REFERENCES category(category_id),
  FOREIGN KEY (type_id)     REFERENCES type    (type_id),
  FOREIGN KEY (status_id)   REFERENCES status  (status_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='個別資料テーブル';


-- ===================================================================
-- 8. 予約テーブル (reservation) ★★★修正箇所★★★
-- ===================================================================
CREATE TABLE reservation (
  reserve_id   INT    NOT NULL AUTO_INCREMENT COMMENT '予約ID',
  book_id      INT    NOT NULL                COMMENT '書誌情報ID',
  member_id    INT    NOT NULL                COMMENT '会員ID',
  reserve_date DATE   NOT NULL                COMMENT '予約日',
  PRIMARY KEY (reserve_id),
  FOREIGN KEY (book_id)   REFERENCES books   (book_id),
  FOREIGN KEY (member_id) REFERENCES members (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='予約テーブル';


-- ===================================================================
-- 9. 貸出テーブル (lending)
-- ===================================================================
CREATE TABLE lending (
  lending_id  INT    NOT NULL AUTO_INCREMENT COMMENT '貸出ID',
  item_id     INT    NOT NULL                COMMENT '個別資料ID',
  member_id   INT    NOT NULL                COMMENT '会員ID',
  lend_date   DATE   NOT NULL                COMMENT '貸出日',
  return_date DATE   NULL                    COMMENT '返却日',
  due_date    DATE   NOT NULL                COMMENT '返却期限',
  PRIMARY KEY (lending_id),
  FOREIGN KEY (item_id)   REFERENCES items   (item_id),
  FOREIGN KEY (member_id) REFERENCES members (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貸出テーブル';


-- ===================================================================
-- 10. 職員テーブル (staff)
-- ===================================================================
CREATE TABLE staff (
  staff_id   INT           NOT NULL AUTO_INCREMENT COMMENT '職員ID',
  name       VARCHAR(20)   NOT NULL                COMMENT '氏名',
  role       VARCHAR(10)   NOT NULL                COMMENT '権限 (受付／管理者)',
  password   VARCHAR(100)  NOT NULL                COMMENT 'パスワード (ハッシュ化して格納)',
  PRIMARY KEY (staff_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='職員テーブル';