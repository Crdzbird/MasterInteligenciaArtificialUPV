import stanza

# Carga del modelo en español
nlp = stanza.Pipeline('es')

# Lectura del archivo
with open("Alicia.txt", "r", encoding="utf-8") as file:
    text = file.read()

# Procesamiento del texto
doc = nlp(text)

# Guardar etiquetado morfosintáctico en archivo
with open("output_stanza.txt", "w", encoding="utf-8") as outfile:
    for sentence in doc.sentences:
        for word in sentence.words:
            outfile.write(f"{word.text}\t{word.pos}\n")
