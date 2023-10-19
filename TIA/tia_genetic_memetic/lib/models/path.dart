class Path implements Comparable<Path> {
  final int from;
  final int to;

  Path(this.from, this.to);

  int get getFrom => from;

  int get getTo => to;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    if (other is Path) {
      return from == other.from && to == other.to;
    }

    return false;
  }

  @override
  int get hashCode => from.hashCode ^ to.hashCode;

  @override
  int compareTo(Path other) {
    int cmp = from.compareTo(other.from);
    return cmp != 0 ? cmp : to.compareTo(other.to);
  }
}
