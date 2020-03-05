(ns hello-h2.fasta)

;;
;; See e.g.GenBank dor SARS-CoV-2 [1].
;;
;; [1] https://www.ncbi.nlm.nih.gov/genome/genomes/86693
;;
;; Curl can list FTP dirs too, just add a trailing slash:
;;
;;    $ curl ftp://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/009/858/895/GCA_009858895.3_ASM985889v3/
;;    $ curl -LO ftp://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/009/858/895/GCA_009858895.3_ASM985889v3/GCA_009858895.3_ASM985889v3_genomic.fna.gz
;;    $ zless GCA_009858895.3_ASM985889v3_genomic.fna.gz | md5sum
;;    cacc9084842139321adc1f83326d6819  -
;;

;; MN908947.3 Severe acute respiratory syndrome coronavirus 2 isolate
;; Wuhan-Hu-1, complete genome
(def url (str "ftp://ftp.ncbi.nlm.nih.gov"
              "/genomes/all/GCA/009/858/895/GCA_009858895.3_ASM985889v3"
              "/GCA_009858895.3_ASM985889v3_genomic.fna.gz"))

;; Clojure "slurp" will happily download  and slurp a binary file too,
;; but the String would be garbage. We need this:
(defn- slurp-gz [url]
  (with-open [in (java.util.zip.GZIPInputStream.
                  (clojure.java.io/input-stream url))]
    (slurp in)))

;; FIXME: very fragile. Always assumes header line:
(defn parse-fasta [text]
  (let [lines (clojure.string/split-lines text)
        header (first lines)
        description (subs header 1)
        sequence (apply str (rest lines))]
    {:description description, :sequence sequence}))

;; SARS-CoV-2 ist about 30Kb: (count (slurp-gz url)) = 30373. Of which
;; only (count (:sequence (parse-fasta faste)) = 29903.
(defn- main []
  (let [fasta (slurp-gz url)]
    (parse-fasta fasta)))
