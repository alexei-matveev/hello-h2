;;
;; This is a  hot topic at the beginning of  2020, understandably. See
;; e.g. Genbank for SARS-CoV-2 [1].
;;
;; [1] https://www.ncbi.nlm.nih.gov/nuccore/MN908947
;;
;; TIL, curl  can list FTP  dirs too, just  add a trailing  slash!
;;
;;     $ curl -LO ftp://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/009/858/895/GCA_009858895.3_ASM985889v3/GCA_009858895.3_ASM985889v3_genomic.fna.gz
;;
;;     $ zless GCA_009858895.3_ASM985889v3_genomic.fna.gz | md5sum
;;     cacc9084842139321adc1f83326d6819  -
;;
;;     $ zless resources/GCA_009858895.3_ASM985889v3_genomic.fna.gz | head  -3
;;     >MN908947.3 Severe acute respiratory syndrome coronavirus 2 isolate Wuhan-Hu-1, complete genome
;;     ATTAAAGGTTTATACCTTCCCAGGTAACAAACCAACCAACTTTCGATCTCTTGTAGATCTGTTCTCTAAACGAACTTTAA
;;     AATCTGTGTGGCTGTCACTCGGCTGCATGCTTAGTGCACTCACGCAGTATAATTAATAACTAATTACTGTCGTTGACAGG
;;
;; The FTP  URLs are hard to  find.  Entrez Tools make  a Docker image
;; available [3]:
;;
;;     $ docker run --rm ncbi/edirect efetch -db nucleotide -id MN908947 -format gb
;;
;; [3] https://github.com/ncbi/docker/tree/master/edirect
;;
(ns hello-h2.fasta)


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

;;
;; FIXME: very fragile. Always assumes a header line. See FASTA Format
;; [2].
;;
;; [2] https://en.wikipedia.org/wiki/FASTA_format
;;
(defn parse-fasta [text]
  (let [lines (clojure.string/split-lines text)
        header (first lines)
        description (subs header 1)
        sequence (apply str (rest lines))]
    {:description description, :sequence sequence}))

;; SARS-CoV-2 genom is exactly 29903 long [1].  (count (slurp-gz url))
;; =  30373. Of  which only  (count (:sequence  (parse-fasta text))  =
;; 29903.
(defn- main []
  (let [fasta (slurp-gz url)]
    (parse-fasta fasta)))
