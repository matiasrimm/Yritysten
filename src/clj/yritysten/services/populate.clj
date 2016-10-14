(ns tut.scr
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [yritysten.db.core :as db]))

(mount.core/start)

;; first get all companies basic data

(def ^:dynamic prhapi "http://avoindata.prh.fi:80/tr/v1?")
(def ^:dynamic ress "false")
(def ^:dynamic mxresults "1000")
(def ^:dynamic resultsfr "0")
(def ^:dynamic date "1950-01-01")

(def agento (agent {}))
(def formatter (f/formatters :date))

(defn totalresults [b] (str "totalResults=" b))
(defn maxresults [mx] (str "&maxResults=" mx))
(defn resultsfrom [f] (str "&resultsFrom=" f))
(defn registerationdates [date] (str "&companyRegistrationFrom=" date "&companyRegistrationTo=" date))

(defn getCompanies [date]
  (str prhapi (totalresults ress)
       (maxresults mxresults)
       (resultsfrom resultsfr)
       (registerationdates date)))

(defn getAndReturnJson[n]
 (json/read-str(:body (client/get n)):key-fn keyword))

(defn persistCompanies [d]
 (doseq [s
   (:results (json/read-str (:body
     (client/get (getCompanies d))) :key-fn keyword))]
      (send-off agento (fn [state]
        (db/create-business!
          {:name (:name s)
           :registrationDate (:registrationDate s)
           :email (:email s)
           :website (:website s)
           :phone (:phone s)
           :companyForm (:companyForm s)
           :detailsUri (:detailsUri s)
           :bisDetailsUri (:bisDetailsUri s)})                      
        (assoc state :done true)))
        (await agento)))

(defn decByDay[d]
  (f/unparse formatter
  (t/minus (f/parse formatter d) (t/days 1))))

(def todaysDate
  (f/unparse formatter (t/now)))

(defn cook[dated]
  (loop [s dated]
    (when (not= s "2000-01-01")
      (try
        (persistCompanies s)
        (catch Exception e))
        (Thread/sleep 250)
          (recur(decByDay s)))))

;; now you got the companies, get the additional infos from links

(def recordCount (+ 1 (:count (db/get-records-count))))

(defn getDetailuri[bizId]
  (:detailsuri (db/get-business {:id bizId})))

(defn getWebsite [n]
  (get-in (getAndReturnJson (getDetailuri n))
    [:results 0 :addresses 0]))

(defn updateWebAddress[id uri phone]
  (db/update-business {:id id :website uri :phone phone}))

(defn updateDetailuri[]
  (loop [n 1]
    (let [site (getWebsite n)]
      (when (not= n recordCount)
       (updateWebAddress
         n
         (:website site)
         (:phone site))
           (Thread/sleep 250)
             (recur (inc n))))))

(updateDetailuri)
