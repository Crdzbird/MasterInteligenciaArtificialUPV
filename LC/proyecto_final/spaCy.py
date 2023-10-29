import spacy

# Carga del modelo en español
nlp = spacy.load("es_core_news_sm")

# Lectura del archivo
with open("Alicia.txt", "r", encoding="utf-8") as file:
    text = file.read()

# Procesamiento del texto
doc = nlp(text)

# Etiquetado morfosintáctico
with open("output_spacy.txt", "w", encoding="utf-8") as outfile:
    for token in doc:
        outfile.write(f"{token.text}\t{token.pos_}\n")