//package com.imagepot.xyztk.util.annotation;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//
//@Documented // javadoc文章化
//@Constraint(validatedBy = { UnusedValidator.class }) // 具体的なチェックロジックを実装したクラス
//@Target({ ElementType.FIELD }) // フィールド・ENUMがアノテーション対象
//@Retention(RetentionPolicy.RUNTIME) // クラスファイルに記録されるかつ実行時JVMにアノテーション情報が読み込まれる
//public @interface Unused {
//
//    // エラーメッセージ
////    String message() default "{validation.Unused.message}";
//
//    // バリデーションのグループ化
//    Class<?>[] groups() default {};
//
//    // チェック対象のオブジェクトにメタ情報を与える場合実装
//    Class<? extends Payload>[] payload() default {};
//
//    @Target({ ElementType.FIELD })
//    @Retention(RetentionPolicy.RUNTIME)
//    @Documented
//    public @interface List {
//        Unused[] value();
//    }
//}
