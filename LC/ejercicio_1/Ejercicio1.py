import re


def tokenize(text):
    pattern = (
        r"(http://\S+|https://\S+)|"
        r"(\w+@\w+\.\w+)|"
        r"(\d{1,2}[-/]\d{1,2}[-/]\d{2,4})|"
        r"(\d{1,2}:\d{1,2})|"
        r"(\d+,\d+|\d+\.\d+)|"
        r"(\w+\s\w+\s\w+)|"
        r"([A-Z]\.[A-Z]\.)|"
        r"([A-Z]\.[A-Z]\.\S+\.)|"
        r"([A-Za-z]+\s\d{1,2},\s\d{4})|"
        r"(@[\w_]+)|"
        r"(#[\w_]+)|"
        r"([A-Za-z]\.)|"
        r"(?<!\d)(?<!#)(?<!@)(?<![A-Z])[.,](?!\d)(?![A-Z])|"
        r"(?<=\d),(?!\d)|"
        r"[‘“?¿!¡…;:]|"
        r"(?:[\u2600-\u26FF]|[\u2700-\u27BF]|[\u1F600-\u1F64F]|[\u1F300-\u1F5FF]|[\u1F680-\u1F6FF]|[\u1F700-\u1F77F]|[\u1F780-\u1F7FF]|[\u1F800-\u1F8FF]|[\u1F900-\u1F9FF]|[\u1FA00-\u1FA6F]|[\u1FA70-\u1FAFF])"
    )

    tokens_match = re.findall(pattern, text, re.MULTILINE)
    tokens_match = [token for sublist in tokens_match for token in sublist if token]
    return tokens_match

with open("input_tokenizer_2023.txt", "r", encoding="utf8") as f:
    content = f.read()

tokens = tokenize(content)

with open("output_tokens_2023.txt", "w", encoding="utf8") as f:
    for token in tokens:
        f.write(token + "\n")

print("Tokenization complete!")