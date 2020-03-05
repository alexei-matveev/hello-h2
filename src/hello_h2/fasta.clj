(ns hello-h2.fasta)

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

;; Clojure  "slurp" will  happily  download the  binars  too, but  the
;; String would be garbage:
(defn- slurp-gz [url]
  (with-open [in (java.util.zip.GZIPInputStream.
                  (clojure.java.io/input-stream url))]
    (slurp in)))

(defn- main []
  (let [fasta (slurp-gz url)]
    ;; SARS-CoV-2 ist about 30Kb. Here Count = 30373:
    (count (fasta))))
