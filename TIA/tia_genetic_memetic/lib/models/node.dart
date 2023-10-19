class Node<E> implements Comparable<Node<E>> {
  final E value;
  final double h;
  final double g;
  final Node<E>?
      parent; // In Dart, we need to specify if a variable can be null using "?"

  Node(this.value, this.g, this.h, this.parent);

  double get cost => h + g;

  Node<E>? get getParent => parent;

  E get getValue => value;

  double get getH => h;

  double get getG => g;

  @override
  int compareTo(Node<E> other) {
    return other.cost.compareTo(
        cost); // Dart doesn't have a direct method for comparing doubles
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    if (other is Node<E>) {
      return value == other.value;
    }

    return false;
  }

  @override
  int get hashCode => value.hashCode;
}
