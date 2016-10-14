(ns yritysten.test.db.core
  (:require [yritysten.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [yritysten.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'yritysten.config/env
      #'yritysten.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/create-user!
               t-conn
               {:name         "Teppo"
                :registrationDate "12.12.2012"
                :email  "teppo@gmail.com"
                :website      "www.google.com"
                :pass       "pass"
                :phone       "0401234567"
                :companyForm       "TMI"
                :detailsUri       "www.details.fi"
                :bisDetailsUri       "www.bisnessdetails.fi"})))


    (is (= {:id         "1"
            :name         "Teppo"
            :registrationDate "12.12.2012"
            :email  "teppo@gmail.com"
            :website      "www.google.com"
            :pass       "pass"
            :phone       "0401234567"
            :companyForm       "TMI"
            :detailsUri       "www.details.fi"
            :bisDetailsUri       "www.bisnessdetails.fi"}
           (db/get-user t-conn {:id "1"})))))
