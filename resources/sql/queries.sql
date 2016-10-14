-- :name create-business! :! :n
-- :doc creates a new biz rec
INSERT INTO businesses
(name, registrationDate, email, website, phone, companyForm, detailsUri, bisDetailsUri)
VALUES (:name, :registrationDate, :email, :website, :phone, :companyForm, :detailsUri, :bisDetailsUri)

-- :name get-business :? :1
-- :doc select business with given name
SELECT * FROM businesses
WHERE id = :id

-- :name get-records-count :? :1
-- :doc get number of records in table
SELECT COUNT(*) AS count
FROM businesses

-- :name update-business :! :n
UPDATE businesses
SET website = :website,
phone = :phone
WHERE id = :id
