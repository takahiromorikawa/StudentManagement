![](images/StudentManagement.png)

## サービス概要
このプロジェクトは、IT技術を教えるスクールが受講生の情報を保持・分析するための、「受講生管理システム」です。

スクールの運営者が使用することを想定しており、CURD操作中心のシンプルで分かりやすい設計を目指しています。

## 作成背景
JavaやSpring Bootの学習成果を形にするために作成しました。
実務で頻繁に使用される以下の技術やツールを採用しています。

## 主な使用技術
### バックエンド

![Java](https://shields.io)
![Spring Boot](https://shields.io)

### DB
![MySQL](https://shields.io)

### 使用ツール
![MyBatis](https://shields.io)
![JUnit5](https://shields.io)
![Postman](https://shields.io)
![Swagger](https://shields.io)
![Git](https://shields.io)
![GitHub](https://shields.io)
![IntelliJ IDEA](https://shields.io)

## 機能一覧
| 機能                          | 内容                                                                  |
|:----------------------------|:--------------------------------------------------------------------|
| <nobr>受講生詳細の一覧検索            | <nobr>受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。                            |
| <nobr>受講生詳細の個別検索(受講生ID指定)   | <nobr>受講生詳細検索の個別検索です。受講生IDを指定し、一意の受講生詳細を取得します。                      |
| <nobr>受講生詳細の条件検索            | <nobr>名前・受講コース・申込状況などの検索条件を指定し、条件に該当する受講生詳細を取得します。                  |
| <nobr>受講生詳細の登録              | 名前や居住地域などの受講生の情報と、受講コース・申込状況をセットで登録します。                             |
| <nobr>受講生詳細の更新              | 受講生IDを指定し、任意の受講生詳細を更新します。<br>※削除処理については論理削除として実装しているため、更新処理として行います。 |

## 使用イメージ
### 受講生一覧画面
#### 検索

#### 新規登録

#### 削除

### 受講生詳細画面
#### 編集

#### 申込状況を更新

### フォームバリデーション

## 設計書
### API仕様書
[SwaggerによるAPI仕様書](http://localhost:8080/swagger-ui/index.html)

### ER図(Entity-Relationship Diagram)
- 1人の受講生が複数コース持てます(1 : N)<br>
- 1つのコースに対して複数ステータス(仮申込、本申込、受講中、受講終了)が持てます(1 : N)
![](images/ER図.png)

### APIのURL設計
| HTTP<br>メソッド | URL              | 処理内容       |
|:-------------|:-----------------|:-----------|
| GET          | /studentList     | 受講生詳細の一覧検索 |
| GET          | /student/{id}    | 受講生詳細の個別検索(受講生ID指定)           |
| POST         | /RegisterStudent | 受講生詳細の登録 |
| PUT          | /UpdateStudent   | 受講生詳細の更新 |

### 画面遷移図

### シーケンス図
#### 受講生詳細の個別検索(受講生ID指定)フロー
![](images/受講生ID検索.png)
#### 受講生詳細の条件検索フロー
![](images/受講生条件検索.png)
#### 受講生詳細の登録フロー
![](images/受講生登録.png)
#### 受講生詳細の更新フロー
![](images/受講生更新.png)
#### 申込状況の更新フロー
![](images/申込状況更新.png)

## テスト
JUnitを用いて単体テストを実装しました。

### テストを行ったクラス
Controller
- CourseStatusController
- StudentController

Converter
- StudentConverter

Servece
- CourseStatusServece
- StudentService

Repository
- CourseStatusMapper
- StudentCourseMapper
- StudentMapper

## 力を入れたところ

## 今後の課題


















