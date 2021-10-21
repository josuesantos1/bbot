(ns poc-buzzbot.core
  (:gen-class)
  (:require [telegrambot-lib.core :as tbot]
            
            [poc-buzzbot.handlers.poll :as poll]))

(def bot (tbot/create "1733442355:AAENwoSrEc188o63H4clZO9JLexGMTTo_mU"))

(def question "qual o seu nome?")
(def opts ["julio", "josue", "bela"])

(def offset (atom 0))
(def init (atom true))
;; (swap! offset last-message)

(defn get-last
  [result]
  (let [result (get-in (tbot/get-updates bot  {:offset @offset}) [:result])
        last-message (get-in (last result) [:update_id])]
    (reset! offset last-message)
    (reset! init false)
    (println last-message)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (while true 
    (let [result (get-in (tbot/get-updates bot  {:offset @offset}) [:result])
          last-message (get-in (last result) [:update_id])]
      (if (true? @init)
        (get-last result)
        (doseq [x result]
          (let [chat-id (get-in x [:message :chat :id])]
            (if (= last-message @offset)
              (println "=====")
              (do
                (poll/new-poll bot chat-id question opts)
                (reset! offset last-message)))))))))

;; (doseq [x result]
;;   (let [chat-id (get-in x [:message :chat :id])]
;;     (if (= last-message @offset)
;;       (println "=====")
;;       (do
;;         (poll/new-poll bot chat-id question opts)
;;         (reset! offset last-message)))))